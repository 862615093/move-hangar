package snegrid.move.hangar.mqtt.message;

import lombok.*;

import java.io.Serializable;

/**
 * 指令发送 消息体
 *
 * @author wangwei
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonSendCmd<T> implements Serializable {

    /**
     * 设备编号
     */
    private String deviceNumber;

    /**
     * 指令code
     */
    private Integer msgType;

    /**
     * 登录用户id
     */
    private Long sessionId;

    /**
     * 参数体
     */
    private T params;
}
