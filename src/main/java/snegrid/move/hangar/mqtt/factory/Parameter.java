package snegrid.move.hangar.mqtt.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义mqtt消息报文
 *
 * @author wangwei
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Parameter {

    /**
     * 主题前缀
     */
    String topicPrefix;

    /**
     * 主题后缀
     */
    String topicSuffix;

    /**
     * 报文
     */
    String message;
}
