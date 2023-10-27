package snegrid.move.hangar.minio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MinioUploadParam
 * @Description: 上传参数
 * @Author zhaojinglong
 * @Date 2023/09/06
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MinioUploadParam {
    /**
     * 桶名称
     */
    private String bucket;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 字节流
     */
    private byte[] bytes;
    /**
     * 文件类型
     */
    private String contentType;
    /**
     * 文件前缀
     */
    private String prefix;

    /**
     * 业务子集key
     */
    private String businessType;
}
