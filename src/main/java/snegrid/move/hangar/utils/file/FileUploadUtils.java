package snegrid.move.hangar.utils.file;

import cn.hutool.core.util.IdUtil;
import snegrid.move.hangar.config.HangarConfig;
import snegrid.move.hangar.constant.Constants;
import snegrid.move.hangar.exception.ServiceException;
import snegrid.move.hangar.utils.common.DateUtils;
import snegrid.move.hangar.utils.common.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 文件上传工具类
 *
 * @author drone
 */
@Slf4j
public class FileUploadUtils {

    /**
     * 默认大小 100M
     */
    public static final long DEFAULT_MAX_SIZE = 100 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 默认上传的地址
     */
    private static String defaultBaseDir = HangarConfig.getProfile();

    public static void setDefaultBaseDir(String defaultBaseDir) {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    public static String getDefaultBaseDir() {
        return defaultBaseDir;
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    public static final String upload(MultipartFile file) throws IOException {
        try {
            return upload(getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException {
        try {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir          相对应用的基目录
     * @param file             上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     */
    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws Exception {
        int fileNamelength = file.getOriginalFilename().length();
        if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new ServiceException("FileUploadUtils.DEFAULT_FILE_NAME_LENGTH");
        }

        assertAllowed(file, allowedExtension);

        String fileName = extractFilename(file);

        File desc = getAbsoluteFile(baseDir, fileName);
        file.transferTo(desc);
        String pathFileName = getPathFileName(baseDir, fileName);
        return pathFileName;
    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = DateUtils.datePath() + "/" + IdUtil.fastUUID() + "." + extension;
        return fileName;
    }

    public static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.exists()) {
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    public static final String getPathFileName(String uploadDir, String fileName) throws IOException {
        int dirLastIndex = HangarConfig.getProfile().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        return pathFileName;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws Exception {
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE) {
            throw new ServiceException("DEFAULT_MAX_SIZE / 1024 / 1024");
        }

        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            throw new ServiceException("文件大小校验异常");
//            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
//                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
//                        fileName);
//            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
//                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
//                        fileName);
//            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
//                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
//                        fileName);
//            } else if (allowedExtension == MimeTypeUtils.VIDEO_EXTENSION) {
//                throw new InvalidExtensionException.InvalidVideoExtensionException(allowedExtension, extension,
//                        fileName);
//            } else if (allowedExtension == MimeTypeUtils.KML_EXTENSION) {
//                throw new InvalidExtensionException.InvalidVideoExtensionException(allowedExtension, extension,
//                        fileName);
//            } else {
//                throw new InvalidExtensionException(allowedExtension, extension, fileName);
//            }
        }

    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        String extension = "FilenameUtils.getExtension(file.getOriginalFilename())";
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }


    public static byte[] getImageFromURL(String urlPath) {
        byte[] data = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            is = conn.getInputStream();
            if (conn.getResponseCode() == 200) {
                data = readInputStream(is);
            } else {
                data = null;
            }
        } catch (MalformedURLException e) {
            log.info("MalformedURLException", e);
        } catch (IOException e) {
            log.info("IOException", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.info("IOException", e);
            }
            conn.disconnect();
        }
        return data;
    }


    public static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[2 * 1024];
        int length;
        try {
            while ((length = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
        } catch (IOException e) {
            log.info("IOException", e);
        }
        byte[] data = baos.toByteArray();
        try {
            is.close();
            bis.close();
            baos.close();
        } catch (IOException e) {
            log.info("IOException", e);
        }
        return data;
    }
}
