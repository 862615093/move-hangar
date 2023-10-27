package snegrid.move.hangar.mqtt.message;

import lombok.*;

/**
 * MCS 返回消息实体
 *
 * @author wangwei
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class McsParams {

    /**
     * 通讯编码
     */
    private Integer code;

    /**
     * 飞行任务记录ID
     */
    private String historyBizId;

    /**
     * 航点编号
     */
    private Integer pointNumber;

    /**
     * 降落模式 0：正常降落  1：手动结束  2：紧急降落
     */
    private Integer landingPattern;

    /**
     * 图片路径
     */
    private String imageUrl;

    /**
     * 返航维度
     */
    private Double backLatitude;

    /**
     * 返航经度
     */
    private Double backLongitude;

    private Double homeLatitude;

    private Double homeLongitude;

    private Double landLatitude;

    private Double landLongitude;

    private Integer fileCurrentNumber;

    private Integer fileMode;
}
