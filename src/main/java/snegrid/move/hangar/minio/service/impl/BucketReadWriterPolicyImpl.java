package snegrid.move.hangar.minio.service.impl;

import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import snegrid.move.hangar.minio.service.BucketPolicyInterface;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName BucketReadWriterPolicyImpl
 * @Description: 读写权限
 * @Author zhaojl
 * @Date 2022/11/7
 **/
public class BucketReadWriterPolicyImpl implements BucketPolicyInterface {
    private static final Logger logger = LoggerFactory.getLogger(BucketReadWriterPolicyImpl.class);
    /**
     * 桶占位符
     */
    private static final String BUCKET_PARAM = "${bucket}";

    /**
     * bucket权限-读写
     */
    private static final String READ_WRITE = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";

    @Override
    public boolean createBucketPolicy(MinioClient client, String bucket) {
        try {
            client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucket).config(READ_WRITE.replace(BUCKET_PARAM, bucket)).build());
            return true;
        } catch (InvalidKeyException | InsufficientDataException | InternalException | InvalidResponseException |
                 NoSuchAlgorithmException | ServerException | XmlParserException | IllegalArgumentException |
                 IOException | io.minio.errors.ErrorResponseException e) {
            e.printStackTrace();
            logger.error("error: {}", e.getMessage(), e);
        }
        return false;
    }

}
