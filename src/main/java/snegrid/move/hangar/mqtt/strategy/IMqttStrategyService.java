package snegrid.move.hangar.mqtt.strategy;

import snegrid.move.hangar.mqtt.factory.Parameter;

/**
 * 策略模式接口
 *
 * @author wangwei
 */
public interface IMqttStrategyService {

    //这个方法对应策略实现类的具体实现
    void processBiz(Parameter dto);

    //这个方法就是策略类的类型，也就是对应```if...else```条件判断的类型
    String getType();
}
