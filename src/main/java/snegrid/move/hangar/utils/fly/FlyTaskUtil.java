package snegrid.move.hangar.utils.fly;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import snegrid.move.hangar.business.enums.OrderMuster;
import snegrid.move.hangar.business.service.L1CacheService;
import snegrid.move.hangar.config.RedisCache;
import snegrid.move.hangar.mqtt.MqttClientHangar;
import snegrid.move.hangar.mqtt.message.CommonSendCmd;
import snegrid.move.hangar.mqtt.message.HangarTelemetryVo;
import snegrid.move.hangar.netty.message.Message;
import snegrid.move.hangar.netty.message.MessageUtil;
import snegrid.move.hangar.netty.model.User;
import snegrid.move.hangar.system.domain.entity.SysDictData;
import snegrid.move.hangar.utils.common.DictUtils;
import snegrid.move.hangar.utils.common.StringUtils;
import snegrid.move.hangar.utils.spring.SpringUtils;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static snegrid.move.hangar.constant.MagicValue.*;
import static snegrid.move.hangar.mqtt.constant.MqttConstant.PUB_TOPIC;
import static snegrid.move.hangar.mqtt.constant.MqttConstant.SUB_TELEMETRY_TOPIC;

/**
 * 飞行任务工具类
 *
 * @author wangwei
 */
@Component
public class FlyTaskUtil {

    private static final Logger logger = LoggerFactory.getLogger(FlyTaskUtil.class);

    private static final L1CacheService l1CacheService = SpringUtils.getBean(L1CacheService.class);

    private static final RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 发送消息到mqtt
     *
     * @param topicSuffix  主题后缀
     * @param msgType      指令编码
     * @param user         连接用户
     * @param deviceNumber 设备编码
     * @param o            消息体
     * @return 结果
     */
    public static Message sendControlMessage(@NotNull String topicSuffix, @NotNull Integer msgType, @NotNull String deviceNumber, @NotNull User user, Object o) {
        if (!sendMessageToMcs(topicSuffix, msgType, deviceNumber, user.getUserId(), o))
            return MessageUtil.getMessage(user, false, OrderMuster.getByMsgType(msgType).getFunction() + "指令下发失败！");
        return MessageUtil.getMessage(user, true, OrderMuster.getByMsgType(msgType).getFunction() + "指令下发成功！");
    }

    /**
     * 发送消息到机库
     *
     * @param topicSuffix  主题后缀
     * @param msgType      指令编码
     * @param userId       用户ID
     * @param hangarNumber 机库编码
     * @param o            消息体
     * @return 结果
     */
    public static boolean sendMessageToHangar(@NotNull String topicSuffix, @NotNull Integer msgType, @NotNull String hangarNumber, @NotNull Long userId, Object o) {
        CommonSendCmd<Object> cmd = CommonSendCmd.builder().msgType(msgType).deviceNumber(hangarNumber).sessionId(userId).params(o).build();
        logger.info("发送消息到MCS的机库topic后缀 =【{}】, cmd = 【{}】", topicSuffix, cmd);
        return MqttClientHangar.publishMessage(PUB_TOPIC.replace(POUND, topicSuffix), JSON.toJSONString(cmd, SerializerFeature.WriteNullStringAsEmpty));
    }

    /**
     * 发送消息到MCS
     *
     * @param topicSuffix  主题后缀
     * @param msgType      指令编码
     * @param userId       用户ID
     * @param hangarNumber 无人机编码
     * @param o            消息体
     * @return 结果
     */
    public static boolean sendMessageToMcs(@NotNull String topicSuffix, @NotNull Integer msgType, @NotNull String hangarNumber, @NotNull Long userId, Object o) {
        CommonSendCmd<Object> cmd = CommonSendCmd.builder().msgType(msgType).deviceNumber(hangarNumber).sessionId(userId).params(o).build();
        logger.info("发送消息到MCS的topic后缀 =【{}】, cmd = 【{}】", topicSuffix, cmd);
        return MqttClientHangar.publishMessage(PUB_TOPIC.replace(POUND, topicSuffix), JSON.toJSONString(cmd, SerializerFeature.WriteNullStringAsEmpty));
    }

    /**
     * 飞前条件检查
     *
     * @param deviceNumber 设备号
     * @return 结果
     */
    public static Map<String, Object> preFlyCheck(@NotNull String deviceNumber) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(FLAG, true);
        List<SysDictData> sysDictData = DictUtils.getDictCache("drone_box_param");
        if (ObjectUtil.isNotNull(sysDictData) && CollectionUtil.isNotEmpty(sysDictData)) {
            try {
                HangarTelemetryVo hangarTelemetryVo = (HangarTelemetryVo) l1CacheService.get(SUB_TELEMETRY_TOPIC.replace(POUND, deviceNumber));
                if (hangarTelemetryVo.getDeviceStatus() != 10) {
                    map.put(FLAG, false);
                    map.put(MSG, "机库非待命状态,无法开始飞行任务！");
                    logger.error("机库非待命状态!");
                    return map;
                }
                for (SysDictData dictData : sysDictData) {
                    if (dictData.getDictValue().equals("MIN_BATTERY")
                            && (Math.min(hangarTelemetryVo.getBatteryPercent11(), hangarTelemetryVo.getBatteryPercent12())) <= Integer.parseInt(dictData.getRemark())) {
                        map.put(FLAG, false);
                        map.put(MSG, "电量不满足！");
                        logger.error("电量不满足!");
                        break;
                    }
                    if (dictData.getDictValue().equals("RAIN_FALL_MAX") && hangarTelemetryVo.getWeatherRainSpeed() >= Integer.parseInt(dictData.getRemark())) {
                        map.put(FLAG, false);
                        map.put(MSG, "雨量不满足！");
                        logger.error("雨量不满足!");
                        break;
                    }
                    if (dictData.getDictValue().equals("RAIN_FALL_MAX") && hangarTelemetryVo.getWeatherWindSpeed() >= Integer.parseInt(dictData.getRemark())) {
                        map.put(FLAG, false);
                        map.put(MSG, "风速不满足！");
                        logger.error("风速不满足!");
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error("飞前条件检查preFlyCheck()方法异常", e);
                map.put(FLAG, false);
                map.put(MSG, "飞前条件检查preFlyCheck()方法异常！");
            }
        } else {
            map.put(FLAG, false);
            map.put(MSG, "字典表drone_box_param校验条件为空！");
        }
        return map;
    }

    /**
     * 结束飞行任务校验
     *
     * @param flyTaskId 飞行任务ID
     * @return 结果
     */
    public static Map<String, Object> endFlyTaskCheck(@NotNull String flyTaskId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(FLAG, true);
        String interruptTaskId = redisCache.getCacheObject("fly_task:interrupt_task:" + flyTaskId);
        if (StringUtils.isNotNull(interruptTaskId)) {
            map.put(FLAG, false);
            map.put(MSG, "无人机已起飞，无法结束飞行任务！");
        }
        return map;
    }
}
