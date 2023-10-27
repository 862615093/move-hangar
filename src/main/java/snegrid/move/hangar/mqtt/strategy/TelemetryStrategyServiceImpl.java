package snegrid.move.hangar.mqtt.strategy;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import snegrid.move.hangar.business.service.L1CacheService;
import snegrid.move.hangar.mqtt.factory.Parameter;
import snegrid.move.hangar.mqtt.message.CommonReceiveCmd;
import snegrid.move.hangar.mqtt.message.DroneTelemetryVo;
import snegrid.move.hangar.mqtt.message.HangarTelemetryMessage;
import snegrid.move.hangar.mqtt.message.HangarTelemetryVo;
import snegrid.move.hangar.netty.handler.CommonNettyHandler;

/**
 * 遥测业务处理
 *
 * @author wangwei
 */
@Service
@AllArgsConstructor
public class TelemetryStrategyServiceImpl implements IMqttStrategyService {

    private static final Logger logger = LoggerFactory.getLogger(TelemetryStrategyServiceImpl.class);

    private final CommonNettyHandler nettyHandler;

    private final L1CacheService l1CacheService;

    @Override
    public void processBiz(Parameter dto) {
        //1.判断遥测类型
        String msg;
        if (dto.getTopicSuffix().startsWith("SNE")) {
            CommonReceiveCmd<HangarTelemetryMessage> commonReceiveCmd = JSON.parseObject(dto.getMessage(), new CommonReceiveCmd<HangarTelemetryMessage>() {
            }.getClass());
            HangarTelemetryVo vo = BeanUtil.copyProperties(commonReceiveCmd.getParams(), HangarTelemetryVo.class);
            //2.更新缓存
            l1CacheService.cacheUpdate(dto.getTopicPrefix() + dto.getTopicSuffix(), vo);
            l1CacheService.cacheUpdate("HangarNumber", dto.getTopicSuffix());
            msg = JSONObject.toJSONString(vo);
        } else {
            DroneTelemetryVo droneTelemetryVo = JSON.parseObject(dto.getMessage(), DroneTelemetryVo.class);
            l1CacheService.cacheUpdate(dto.getTopicPrefix() + dto.getTopicSuffix(), droneTelemetryVo);
            l1CacheService.cacheUpdate("DroneNumber", dto.getTopicSuffix());
            msg = JSONObject.toJSONString(droneTelemetryVo);
        }
        //3.推送遥测到所有在线用户
        nettyHandler.sendMessageToAll(msg);
    }

    @Override
    public String getType() {
        return "/sys/info/";
    }
}
