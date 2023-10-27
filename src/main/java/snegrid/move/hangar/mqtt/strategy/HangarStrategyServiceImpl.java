package snegrid.move.hangar.mqtt.strategy;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import snegrid.move.hangar.business.domain.entity.FlyTask;
import snegrid.move.hangar.business.service.IFlyTaskService;
import snegrid.move.hangar.exception.ServiceException;
import snegrid.move.hangar.mqtt.factory.Parameter;
import snegrid.move.hangar.mqtt.message.CommonReceiveCmd;
import snegrid.move.hangar.netty.handler.CommonNettyHandler;
import snegrid.move.hangar.utils.fly.FlyTaskUtil;

import java.time.LocalDateTime;

/**
 * 机库指令业务处理
 *
 * @author wangwei
 */
@Service
@AllArgsConstructor
public class HangarStrategyServiceImpl implements IMqttStrategyService {

    private static final Logger logger = LoggerFactory.getLogger(HangarStrategyServiceImpl.class);

    private final CommonNettyHandler nettyHandler;

    private final IFlyTaskService flyTaskService;

    @Override
    public void processBiz(Parameter dto) {
        CommonReceiveCmd<?> commonReceiveCmd = JSONObject.parseObject(dto.getMessage(), CommonReceiveCmd.class);
        logger.info("接收机库指令消息实体=【{}】", commonReceiveCmd);
//        String droneNumber = String.valueOf(l1CacheService.get("DroneNumber"));
//        String hangarNumber = String.valueOf(l1CacheService.get("HangarNumber"));
        String hangarNumber = "SNE00001";
        String droneNumber = "1581F6GKB238400400B4";
        Long sessionId = commonReceiveCmd.getSessionId();
        FlyTask lastFlyTask;
        switch (commonReceiveCmd.getMsgType()) {
            case 2000:
                logger.info("2000：机场准备完毕");
                //流程通知
                nettyHandler.sendMessageToAll("机场准备完毕,等待无人机飞前检查完毕起飞！");
                //TODO 消息收集到es
                break;
            case 2100:
                logger.info("2100：机库平台升起完毕允许降落");
                //1.通知无人机，允许降落
                if (!FlyTaskUtil.sendMessageToMcs(droneNumber, 4104, droneNumber, null == sessionId ? 1L : sessionId, null))
                    throw new ServiceException("通知无人机允许降落指令失败！");
                //2.流程通知
                nettyHandler.sendMessageToAll("无人机允许降落指令发送成功！");
                //TODO 3.消息收集到es
                break;
            case 2101:
                logger.info("2101：无人机下降回收到机库平台内完成");
                //1.通知MCS开始上传图片
                if (!FlyTaskUtil.sendMessageToMcs(droneNumber, 4012, droneNumber, null == sessionId ? 1L : sessionId, null))
                    throw new ServiceException("通知MCS开始上传图片指令失败！");
                //2.流程通知
                nettyHandler.sendMessageToAll("通知MCS开始上传图片指令发送成功！");
                //TODO 3.消息收集到es
                break;
            case 2006:
                logger.info("2006：机库回收动作完成");
                //1.更新任务结束时间
                lastFlyTask = flyTaskService.getLastFlyTask();
                flyTaskService.lambdaUpdate().eq(FlyTask::getId, lastFlyTask.getId()).set(FlyTask::getTaskEndTime, LocalDateTime.now()).update();
                //2.流程通知
                nettyHandler.sendMessageToAll("机库回收动作完成！");
                //TODO 3.消息收集到es
                break;
            default:
                logger.error("未知的机库指令！");
                break;
        }
    }

    @Override
    public String getType() {
        return "/sys/jkcmd/";
    }
}
