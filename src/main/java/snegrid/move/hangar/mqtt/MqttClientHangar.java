package snegrid.move.hangar.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import snegrid.move.hangar.constant.MagicValue;
import snegrid.move.hangar.mqtt.factory.MqttStrategyServiceFactory;
import snegrid.move.hangar.utils.common.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author : zhaojl
 * @description :EMQ订阅客户端、
 * @date :  2023年2月6日
 */
@Component
public class MqttClientHangar {

    private static final Logger logger = LoggerFactory.getLogger(MqttClientCallback.class);

    @Value("${mqtt.broker}")
    private String serverURI;

    @Value("${mqtt.clientId}")
    private String clientId;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.timeout}")
    private int timeout;

    @Value("${mqtt.keepAliveInterval}")
    private int keepAliveInterval;

    @Value("${mqtt.qos}")
    private int qos;

    private static int qos1;

    @Value("${mqtt.topicSub}")
    private String topicSub;

    @Value("${mqtt.topicPub}")
    private String topicPub;

    private static MqttClient client;

    @Resource
    private MqttStrategyServiceFactory mqttStrategyServiceFactory;

    @Resource
    private ThreadPoolTaskExecutor poolTaskExecutor;

    private MqttConnectOptions options;

    @PostConstruct
    private void start() {
        try {
            MqttClientHangar.qos1 = qos;
            init();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

    public void init() {
        try {
            client = new org.eclipse.paho.client.mqttv3.MqttClient(serverURI, clientId, new MemoryPersistence());
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(timeout);
            // 设置会话心跳时间 单位为秒
            options.setKeepAliveInterval(keepAliveInterval);
            // 设置自动重连
            options.setAutomaticReconnect(true);
            client.connect(options);
            String[] topicSubs = StringUtils.split(topicSub, MagicValue.COMMA);
            client.subscribe(topicSubs);
            // 此处使用的MqttCallbackExtended类而不是MqttCallback，是因为如果emq服务出现异常导致客户端断开连接后，重连后会自动调用connectComplete方法
            // 创建回调函数对象
            client.setCallback(new MqttClientCallback(this, mqttStrategyServiceFactory, poolTaskExecutor));
            logger.info("MqttClient初始化成功....");
        } catch (MqttException e) {
            e.printStackTrace();
            logger.error("MqttClient初始化失败", e);
        }
    }

    /**
     * 断开重连,重新订阅之前的额topic,五秒重试一次
     */
    public synchronized void reConnect() {
        while (true) {
            if (null != client) {
                if (!client.isConnected()) {
                    if (null != options) {
                        try {
                            client.connect();
                            client.setCallback(new MqttClientCallback(this, mqttStrategyServiceFactory, poolTaskExecutor));
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        if (client.isConnected()) {
                            String[] topicSubs = StringUtils.split(topicSub, MagicValue.COMMA);
                            try {
                                client.subscribe(topicSubs);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    } else {
                        logger.info("mqttConnectOptions is null");
                    }
                } else {
                    logger.info("mqttClient is null or connect");
                }
            } else {
                init();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void end() {
        try {
            client.disconnect();
            client.close();
        } catch (MqttException e) {
            logger.error("MqttClient断开连接失败", e);
        }
    }

    public static boolean publishMessage(String pubTopic, String message) {
        if (null != client && client.isConnected()) {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setQos(qos1);
            mqttMessage.setPayload(message.getBytes());
            MqttTopic topic = client.getTopic(pubTopic);
            if (null != topic) {
                try {
                    MqttDeliveryToken publish = topic.publish(mqttMessage);
//                    logger.info("publishMessage(),消息发布成功!");
                    return true;
                } catch (MqttException e) {
//                    logger.error("publishMessage(),消息发布失败!", e);
                    return false;
                }
            }
            return false;
        }
        logger.error("publishMessage(),mqtt客户端连接异常!");
        return false;
    }
}
