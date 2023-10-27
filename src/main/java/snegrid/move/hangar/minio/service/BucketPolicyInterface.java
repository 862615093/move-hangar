package snegrid.move.hangar.minio.service;

import io.minio.MinioClient;

/**
 * @description: 设置桶访问策略接口
 * @author: zhaojinglong
 * @date: 2021/11/10
 * @param:
 * @return:
 */
public interface BucketPolicyInterface {
    /**
     * 设置桶策略
     *
     * @param client
     * @param bucket
     * @return
     */
    boolean createBucketPolicy(MinioClient client, String bucket);
}
