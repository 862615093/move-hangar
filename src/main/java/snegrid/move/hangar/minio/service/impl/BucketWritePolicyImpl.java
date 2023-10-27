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
 * @ClassName BucketWritePolicy
 * @Description: 只写实现类
 * @Author zhaojl
 * @Date 2022/11/7
 **/
public class BucketWritePolicyImpl implements BucketPolicyInterface {
    public static final Logger logger = LoggerFactory.getLogger(BucketWritePolicyImpl.class);
    /**
     * 桶占位符
     */
    private static final String BUCKET_PARAM = "${bucket}";
    /**
     * bucket权限-只读
     */
    private static final String WRITE_ONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\"],\"Resource\":[\"arn:aws:s3:::" + BUCKET_PARAM + "/*\"]}]}";

    @Override
    public boolean createBucketPolicy(MinioClient client, String bucket) {
        // TODO Auto-generated method stub
        try {
            client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucket).config(WRITE_ONLY.replace(BUCKET_PARAM, bucket)).build());
            return true;
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                 | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                 | IllegalArgumentException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("error: {}", e.getMessage(), e);
        }
        return false;
    }

}
