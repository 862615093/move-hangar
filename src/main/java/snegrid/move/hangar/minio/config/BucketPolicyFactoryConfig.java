package snegrid.move.hangar.minio.config;


import snegrid.move.hangar.minio.constant.BucketPolicyConstant;
import snegrid.move.hangar.minio.service.BucketPolicyInterface;
import snegrid.move.hangar.minio.service.impl.BucketReadPolicyImpl;
import snegrid.move.hangar.minio.service.impl.BucketReadWriterPolicyImpl;
import snegrid.move.hangar.minio.service.impl.BucketWritePolicyImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BuckerPolicyFactory
 * @Description: 策略工厂类
 * @Author zhaojinglong
 * @Date 2021/11/10
 **/
public class BucketPolicyFactoryConfig {
    static Map<String, BucketPolicyInterface> operationMap = new HashMap<>();

    static {
        // 只读
        operationMap.put(BucketPolicyConstant.READ, new BucketReadPolicyImpl());
        // 只写
        operationMap.put(BucketPolicyConstant.WRITE, new BucketWritePolicyImpl());
        // 读写
        operationMap.put(BucketPolicyConstant.READ_WRITE, new BucketReadWriterPolicyImpl());
    }

    public static BucketPolicyInterface getBucketPolicyInterface(String poliy) {
        BucketPolicyInterface object = operationMap.get(poliy);
        if (object == null) {
            object = new BucketReadPolicyImpl();
        }
        return object;
    }

}
