package snegrid.move.hangar.minio.service.impl;

import io.minio.MinioClient;
import snegrid.move.hangar.minio.service.BucketPolicyInterface;

/**
 * @ClassName BucketDefaultPolicy
 * @Author zhaojl
 * @Date 2022/11/7
 **/
public class BucketDefaultPolicyImpl implements BucketPolicyInterface {
    @Override
    public boolean createBucketPolicy(MinioClient client, String bucket) {
        return false;
    }
}
