package snegrid.move.hangar.mqtt.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import snegrid.move.hangar.mqtt.strategy.IMqttStrategyService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工厂类
 *
 * @author wangwei
 */
@Component
public class MqttStrategyServiceFactory implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(MqttStrategyServiceFactory.class);

    private final Map<String, IMqttStrategyService> map = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(IMqttStrategyService.class)
                .values()
                .forEach(strategyService -> map.put(strategyService.getType(), strategyService));
    }

    public void processBiz(Parameter dto) {
        IMqttStrategyService mqttStrategyService = map.get(dto.getTopicPrefix());
        if (mqttStrategyService != null) {
            mqttStrategyService.processBiz(dto);
            return;
        }
        logger.error("策略类为空！");
    }
}