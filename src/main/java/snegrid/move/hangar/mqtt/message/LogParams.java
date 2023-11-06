package snegrid.move.hangar.mqtt.message;

import lombok.*;

/**
 * 日志 返回消息实体
 *
 * @author wangwei
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogParams {

    /**
     * 日志等级
     */
    private Integer level;

    /**
     * 编码
     */
    private Integer code;

    /**
     * 日志详情
     */
    private String details;

    /**
     * 时间
     */
    private String createTime;
}
