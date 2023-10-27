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
 * @ClassName BucketReadPolicy
 * @Description: 只读实现类
 * @Author zhaojl
 * @Date 2022/11/7
 **/
public class BucketReadPolicyImpl implements BucketPolicyInterface {
    public static final Logger logger = LoggerFactory.getLogger(BucketReadPolicyImpl.class);
    /**
     * 桶占位符
     */
    private static final String BUCKET_PARAM = "${bucket}";
    /**
     * bucket权限-只读
     */
    private static final String READ_ONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";

    @Override
    public boolean createBucketPolicy(MinioClient client, String bucket) {
        try {
            client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucket).config(READ_ONLY.replace(BUCKET_PARAM, bucket)).build());
            return true;
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                 | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                 | IllegalArgumentException | IOException e) {
            e.printStackTrace();
            logger.error("error: {}", e.getMessage(), e);
        }
        return false;
    }

}
