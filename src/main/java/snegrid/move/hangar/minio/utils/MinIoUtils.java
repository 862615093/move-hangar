package snegrid.move.hangar.minio.utils;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import snegrid.move.hangar.constant.MagicValue;
import snegrid.move.hangar.minio.config.BucketPolicyFactoryConfig;
import snegrid.move.hangar.minio.config.MinioConfig;
import snegrid.move.hangar.minio.constant.BucketPolicyConstant;
import snegrid.move.hangar.minio.service.BucketPolicyInterface;
import snegrid.move.hangar.utils.file.FileUploadUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName MinIoUtils
 * @Description: MinIoUtils
 * @Author zhaojl
 * @Date 2022/11/7
 **/
@Component
public class MinIoUtils {
    Logger log = LoggerFactory.getLogger(MinIoUtils.class);
    @Autowired
    private MinioConfig minioConfig1;

    private MinioClient minioClient;

    /**
     * 初始化minio配置
     */
    @PostConstruct
    public void init() {
        try {
            this.minioClient = MinioClient.builder().endpoint(minioConfig1.getUrl())
                    .credentials(minioConfig1.getAccessKey(), minioConfig1.getSecretKey()).build();
            String bucketName = minioConfig1.getBucketName();
            //创建一个默认的bucket
            boolean found = this.bucketExists(bucketName);
            if (!found) {
                this.createBucket(bucketName);
            }
        } catch (Exception e) {
            log.error("minio连接客户端初始化失败：", e);
            e.printStackTrace();
        }
    }

    /**
     * 判断bucket是否存在
     *
     * @param bucket
     * @return
     */
    public boolean bucketExists(String bucket) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        } catch (Exception e) {
            log.error("判断bucket是否存在失败：", e);
        }
        return false;
    }

    /**
     * 创建bucket
     *
     * @param bucket
     */
    public boolean createBucket(String bucket) {
        try {
            boolean isExist = this.bucketExists(bucket);
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                setBucketPolicy(bucket, BucketPolicyConstant.READ_WRITE);
            }
            return true;
        } catch (Exception e) {
            log.error("创建bucket失败：", e);
        }
        return false;
    }

    /**
     * 创建文件夹
     *
     * @param bucket
     * @param folder 文件夹名称，最后需加“/”，例如：path/to/
     * @return
     */
    public ObjectWriteResponse createFolder(String bucket, String folder) {
        try {
            if (!folder.endsWith(MagicValue.LEFT_SLASH)) {
                folder = folder + MagicValue.LEFT_SLASH;
            }
            return minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucket).object(folder).stream(
                                    new ByteArrayInputStream(new byte[]{}), 0, -1)
                            .build());
        } catch (Exception e) {
            log.error("创建文件夹失败：", e);
        }
        return null;
    }

    /**
     * 创建文件夹
     *
     * @param folder 文件夹名称，最后需加“/”，例如：path/to/
     * @return
     */
    public ObjectWriteResponse createFolder(String folder) {
        return this.createFolder(minioConfig1.getBucketName(), folder);
    }

    /**
     * 文件上传
     *
     * @param bucket      存储桶
     * @param fileName    文件名
     * @param inputStream 文件流
     * @return
     */
    @SneakyThrows
    public ObjectWriteResponse upload(String bucket, String fileName, InputStream inputStream, String contentType) {
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucket).object(fileName).stream(
                                inputStream, -1, FileUploadUtils.DEFAULT_MAX_SIZE).contentType(contentType)
                        .build());
    }

    /**
     * 上传文件
     *
     * @param fileName    文件名
     * @param inputStream 文件流
     * @return
     */
    public ObjectWriteResponse upload(String fileName, InputStream inputStream, String contentType) {
        return this.upload(minioConfig1.getBucketName(), fileName, inputStream, contentType);
    }

    /**
     * 上传文件
     *
     * @param bucket   存储桶
     * @param fileName 文件名
     * @param file     文件对象
     * @return
     */
    public ObjectWriteResponse upload(String bucket, String fileName, MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            return this.upload(bucket, fileName, inputStream, file.getContentType());
        } catch (Exception e) {
            log.error("上传文件失败：", e);
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @param fileName 文件名
     * @param file     文件对象
     * @return
     */
    public ObjectWriteResponse upload(String fileName, MultipartFile file) {
        return this.upload(minioConfig1.getBucketName(), fileName, file);
    }


    /**
     * 上传带路径的文件，自动创建文件夹
     *
     * @param bucket   存储桶名
     * @param filePath 例如：qq1/qq2/test.doc
     * @param file     文件对象
     * @return
     */
    public ObjectWriteResponse uploadWithPath(String bucket, String filePath, MultipartFile file) {
        return this.uploadWithPathMethod(bucket, filePath, file);
    }

    /**
     * 上传带路径的文件，自动创建文件
     *
     * @param filePath 例如：qq1/qq2/test.doc
     * @param file     文件对象
     * @return
     */
    public ObjectWriteResponse uploadWithPathMethod(String bucket, String filePath, MultipartFile file) {
        if (StringUtils.isBlank(bucket)) {
            bucket = minioConfig1.getBucketName();
        }
        int lastSepIndex = filePath.lastIndexOf(MagicValue.LEFT_SLASH);
        if (lastSepIndex > -1) {
            // 创建文件夹
            String folder = filePath.substring(0, lastSepIndex + 1);
            this.createFolder(bucket, folder);
        }
        return this.upload(bucket, filePath, file);
    }


    /**
     * 下载
     *
     * @param filePath 例如：qq1/qq2/qq.doc
     * @param fileName 例如：qq.doc
     * @param response
     */
    public void download(String filePath, String fileName, HttpServletResponse response) {
        this.download(minioConfig1.getBucketName(), filePath, fileName, response);
    }

    /**
     * 下载
     *
     * @param bucketName
     * @param filePath   例如：qq1/qq2/qq.doc
     * @param fileName   例如：qq.doc
     * @param response
     */
    public void download(String bucketName, String filePath, String fileName, HttpServletResponse response) {
        InputStream is = null;
        OutputStream out = null;
        try {
            // 获取对象的元数据
            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(filePath).build());
            response.setContentType(stat.contentType());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            is = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(filePath).build());
            out = response.getOutputStream();
            IOUtils.copy(is, out);
        } catch (Exception e) {
            log.error("下载文件失败:", e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("下载文件后关闭InputStream失败：", e);
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("下载文件后关闭OutputStream失败：", e);
                }
            }
        }
    }

    /**
     * 预览
     *
     * @param bucketName
     * @param filePath   例如：qq1/qq2/qq.doc
     * @param fileName   例如：qq.doc
     * @param response
     */
    public void preView(String bucketName, String filePath, String fileName, HttpServletResponse response) {
        InputStream is = null;
        OutputStream out = null;
        try {
            // 获取对象的元数据
            String extention = filePath.substring(filePath.lastIndexOf(".") + 1);
            String contentType = "application/pdf";
            if (StringUtils.isNotBlank(extention)) {
                contentType = MapUtils.getString(MagicValue.responseTypeMap, extention.toLowerCase());
            }
            response.setContentType(contentType);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            is = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(filePath).build());
            out = response.getOutputStream();
            IOUtils.copy(is, out);
        } catch (Exception e) {
            log.error("预览文件失败:", e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("预览文件关闭InputStream失败：", e);
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("预览文件关闭OutputStream失败：", e);
                }
            }
        }
    }


    /***
     * 获取文件流
     * @param filePath
     * @return
     */
    public InputStream download(String filePath) {
        return this.download(minioConfig1.getBucketName(), filePath);
    }

    /***
     * 获取文件流
     * @param bucketName
     * @param filePath
     * @return
     */
    public InputStream download(String bucketName, String filePath) {
        try {
            // 获取对象的元数据
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(filePath).build());
        } catch (Exception e) {
            log.error("下载文件失败:", e);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public boolean remove(String filePath) {
        return this.remove(minioConfig1.getBucketName(), filePath);
    }

    /**
     * 删除文件
     *
     * @param bucket
     * @param filePath
     */
    public boolean remove(String bucket, String filePath) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(filePath).build());
            return true;
        } catch (Exception e) {
            log.error("删除文件失败：", e);
        }
        return false;
    }


    /***
     * 文件复制
     * @param sourceBucketName
     * @param sourceObjectName
     * @param targetBucketNam
     * @param targetObjectName
     * @return
     */
    public boolean copy(String sourceBucketName, String sourceObjectName, String targetBucketNam, String targetObjectName) {
        try {
            minioClient.copyObject(CopyObjectArgs.builder()
                    .source(CopySource.builder().bucket(sourceBucketName).object(sourceObjectName).build())
                    .bucket(targetBucketNam)
                    .object(targetObjectName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("复制文件异常：", e);
            return false;
        }
    }

    /***
     * 文件复制
     * @param sourceObjectName
     * @param targetObjectName
     * @return
     */
    public boolean copy(String sourceObjectName, String targetObjectName) {
        return this.copy(minioConfig1.getBucketName(), sourceObjectName, minioConfig1.getBucketName(), targetObjectName);
    }

    /**
     * 获取文件地址
     *
     * @param objectName
     * @return
     */
    public String getObjectUrl(String objectName) {
        return this.getObjectUrl(minioConfig1.getBucketName(), objectName);
    }

    /**
     * 获取文件地址
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    public String getObjectUrl(String bucketName, String objectName) {
        if (StringUtils.isBlank(objectName)) {
            return null;
        }

        String url = null;
        try {
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(MagicValue.INT_THREEE, TimeUnit.MINUTES)
                    .method(Method.GET)
                    .build());
        } catch (Exception e) {
            log.error("获取文件地址异常：", e);
        }
        return url;
    }

    /**
     * 获取文件永久地址
     *
     * @param objectName
     * @return
     */
    public String getObjectPermanentUrl(String objectName) {
        return this.getObjectPermanentUrl(minioConfig1.getBucketName(), objectName);
    }

    /**
     * 获取文件永久地址
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    public String getObjectPermanentUrl(String bucketName, String objectName) {
        if (StringUtils.isBlank(objectName)) {
            return null;
        }
        return StringUtils.join(minioConfig1.getUrl(), "/", bucketName, "/", objectName);
    }

    /**
     * 设置策略
     *
     * @param bucket
     * @param policy
     * @return
     */
    public boolean setBucketPolicy(String bucket, String policy) {
        boolean isExist = this.bucketExists(bucket);
        if (isExist) {
            BucketPolicyInterface bucketPolicy = BucketPolicyFactoryConfig.getBucketPolicyInterface(policy);
            return bucketPolicy.createBucketPolicy(minioClient, bucket);
        }
        return false;
    }


    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets()
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        return minioClient.listBuckets();
    }


    /**
     * 判断文件是否存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件名
     * @return
     */
    public boolean isObjectExist(String bucketName, String objectName) {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }


    /**
     * 判断文件夹是否存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件夹名称
     * @return
     */
    public boolean isFolderExist(String bucketName, String objectName) {
        boolean exist = false;
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).prefix(objectName).recursive(false).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.isDir() && objectName.equals(item.objectName())) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    @SneakyThrows
    public List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        List<Item> list = new ArrayList<>();
        Iterable<Result<Item>> objectsIterator = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).prefix(prefix)
                        .recursive(recursive).build()
        );
        if (objectsIterator != null) {
            for (Result<Item> result : objectsIterator) {
                Item item = result.get();
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 删除一个文件夹下所有文件
     *
     * @param buckName
     * @param groupName
     */
    public void removeObjectsByDir(String buckName, String groupName) {
        if (bucketExists(buckName)) {
            try {
                Iterable<Result<Item>> results = minioClient.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(buckName)
                                .prefix(groupName)
                                .recursive(true)
                                .build()
                );

                Iterator<Result<Item>> iterator = results.iterator();
                while (iterator.hasNext()) {
                    Result<Item> itemResult = iterator.next();
                    Item item = itemResult.get();
                    String objectName = item.objectName();
                    System.out.println(objectName);
                    minioClient.removeObject(RemoveObjectArgs.builder()
                            .bucket(buckName)
                            .object(objectName)
                            .build());
                }
            } catch (Exception e) {
            }
        }
    }
}
