package snegrid.move.hangar.mqtt.constant;
/**
 * mqtt相关 常量
 *
 * @author wangwei
 */
public class MqttConstant {

    /**
     * 发送 message topic
     */
    public static final String PUB_TOPIC = "/sys/cmd/#";

    /**
     * 订阅 机库和无人机遥测 message topic
     */
    public static final String SUB_TELEMETRY_TOPIC = "/sys/info/#";
}
