package snegrid.move.hangar.mqtt;

import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import snegrid.move.hangar.exception.ServiceException;
import snegrid.move.hangar.mqtt.factory.MqttStrategyServiceFactory;
import snegrid.move.hangar.mqtt.factory.Parameter;

import java.util.concurrent.CompletableFuture;

import static snegrid.move.hangar.constant.MagicValue.LEFT_SLASH;

/**
 * MQTTReceiveCallback
 *
 * @author zhaojl
 */
@Component
@AllArgsConstructor
public class MqttClientCallback implements MqttCallback {

    private static final Logger logger = LoggerFactory.getLogger(MqttClientCallback.class);

    private final MqttClientHangar mqttClientHangar;

    private final MqttStrategyServiceFactory mqttStrategyServiceFactory;

    private final ThreadPoolTaskExecutor poolTaskExecutor;

    @Override
    public void connectionLost(Throwable throwable) {
        mqttClientHangar.reConnect();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        logger.info("MqttClient接收消息主题 : 【{}】, 接收消息内容 : 【{}】", topic, new String(message.getPayload()));
        String topicPrefix = topic.substring(0, topic.lastIndexOf(LEFT_SLASH) + 1);
        logger.info("topicPrefix={}", topicPrefix);
        String topicSuffix = topic.substring(topic.lastIndexOf(LEFT_SLASH) + 1);
        logger.info("topicSuffix={}", topicSuffix);
        CompletableFuture.runAsync(() -> {
            //调用工厂类,策略实现业务
            try {
                mqttStrategyServiceFactory.processBiz(Parameter.builder()
                        .topicPrefix(topicPrefix)
                        .topicSuffix(topicSuffix)
                        .message(new String(message.getPayload()))
                        .build());
            } catch (Exception e) {
                logger.error("processBiz()方法异常！", e);
                throw new ServiceException("processBiz()方法异常！");
            }
        }, poolTaskExecutor);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}
