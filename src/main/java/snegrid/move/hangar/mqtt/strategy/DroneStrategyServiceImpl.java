package snegrid.move.hangar.mqtt.strategy;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import snegrid.move.hangar.business.domain.entity.File;
import snegrid.move.hangar.business.domain.entity.FlyTask;
import snegrid.move.hangar.business.service.IFileService;
import snegrid.move.hangar.business.service.IFlyTaskService;
import snegrid.move.hangar.business.service.L1CacheService;
import snegrid.move.hangar.exception.ServiceException;
import snegrid.move.hangar.mqtt.factory.Parameter;
import snegrid.move.hangar.mqtt.message.CommonReceiveCmd;
import snegrid.move.hangar.mqtt.message.McsParams;
import snegrid.move.hangar.mqtt.message.McsRoute;
import snegrid.move.hangar.netty.handler.CommonNettyHandler;
import snegrid.move.hangar.utils.fly.FlyTaskUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static snegrid.move.hangar.business.constant.AttachmentConstant.ROUTE_TYPE;
import static snegrid.move.hangar.constant.MagicValue.DECIMAL_POINT;
import static snegrid.move.hangar.constant.MagicValue.FORWARD_SLASH;

/**
 * 无人机指令业务处理
 *
 * @author wangwei
 */
@Service
@AllArgsConstructor
public class DroneStrategyServiceImpl implements IMqttStrategyService {

    private static final Logger logger = LoggerFactory.getLogger(DroneStrategyServiceImpl.class);

    private final IFlyTaskService flyTaskService;

    private final IFileService fileService;

    private final L1CacheService l1CacheService;

    private final CommonNettyHandler nettyHandler;

    @Override
    public void processBiz(Parameter dto) {
        CommonReceiveCmd<McsParams> commonReceiveCmd = JSON.parseObject(dto.getMessage(), new CommonReceiveCmd<McsParams>() {
        }.getClass());
//        String droneNumber = String.valueOf(l1CacheService.get("DroneNumber"));
//        String droneNumber = String.valueOf(l1CacheService.get("HangarNumber"));
        String hangarNumber = "SNE00001";
        String droneNumber = "1581F6GKB238400400B4";
        Long sessionId = commonReceiveCmd.getSessionId();
        FlyTask lastFlyTask;
        McsParams mcsParam = commonReceiveCmd.getParams();
        Optional.ofNullable(mcsParam).orElseThrow(() -> new ServiceException("接收MCS消息体为空！"));
        logger.info("接收无人机指令消息实体=【{}】, droneNumber=【{}】, sessionId=【{}】", commonReceiveCmd, droneNumber, sessionId);
        switch (mcsParam.getCode()) {
            case 6001:
                logger.info("6001：发送飞行航线到MCS");
                //1.发送飞行航线
                lastFlyTask = flyTaskService.getLastFlyTask();
                File routeFile = fileService.getAttachmentFileList(lastFlyTask.getRouteId(), ROUTE_TYPE).get(0);
                if (ObjectUtil.isNull(routeFile)) throw new ServiceException("当前飞行任务关联航线不存在！");
                if (!FlyTaskUtil.sendMessageToMcs(droneNumber, 4000, droneNumber, null == sessionId ? 1L : sessionId, McsRoute.builder()
                        .historyBizId(lastFlyTask.getId()).category(0).elpLongitude(0d).elpLatitude(0d).minBattery(40)
                        .kmzFiles(Collections.singletonList(routeFile.getFilePath())).build()))
                    throw new ServiceException("发送飞行航线任务指令失败！");
                //2.更新飞行开始时间
                flyTaskService.lambdaUpdate().eq(FlyTask::getId, lastFlyTask.getId()).set(FlyTask::getFlyStartTime, LocalDateTime.now()).update();
                //3.流程通知
                nettyHandler.sendMessageToAll("发送飞行航线任务指令成功！");
                //TODO 4.消息收集到es
                break;
            case 6007:
                logger.info("6007：MCS航线解析成功通知");
                if (!FlyTaskUtil.sendMessageToMcs(droneNumber, 4002, droneNumber, null == sessionId ? 1L : sessionId, null))
                    throw new ServiceException("发送开始飞行任务指令失败！");
                //2.流程通知
                nettyHandler.sendMessageToAll("发送开始飞行任务指令成功！");
                //TODO 3.消息收集到es
                break;
            case 6002:
                logger.info("6002：无人机起飞成功通知");
                //1.通知机库无人机已起飞，关闭舱门
                if (!FlyTaskUtil.sendMessageToHangar(hangarNumber, 4100, hangarNumber, null == sessionId ? 1L : sessionId, null))
                    throw new ServiceException("通知机库无人机已起飞指令失败！");
                //2.流程通知
                nettyHandler.sendMessageToAll("通知机库无人机已起飞指令发送成功！");
                //TODO 3.消息收集到es
                break;
            case 6003:
                logger.info("6003：无人机返航通知");
                //1.通知机库,无人机返航
                if (!FlyTaskUtil.sendMessageToHangar(hangarNumber, 4101, hangarNumber, null == sessionId ? 1L : sessionId, null))
                    throw new ServiceException("通知机库无人机已返航，开启舱门指令失败！");
                //2.流程通知
                nettyHandler.sendMessageToAll("通知机库无人机已返航，开启舱门指令发送成功！");
                //TODO 3.消息收集到es
                break;
            case 6004:
                logger.info("6004：无人机降落成功通知机库");
                //1.通知机库平台无人机已降落成功
                if (!FlyTaskUtil.sendMessageToHangar(hangarNumber, 4102, hangarNumber, null == sessionId ? 1L : sessionId, null))
                    throw new ServiceException("通知机库无人机已成功降落至平台指令失败！");
                //2.更新飞行任务结束时间
                lastFlyTask = flyTaskService.getLastFlyTask();
                flyTaskService.lambdaUpdate().eq(FlyTask::getId, lastFlyTask.getId()).set(FlyTask::getFlyEndTime, LocalDateTime.now()).update();
                //3.流程通知
                nettyHandler.sendMessageToAll("通知机库无人机已成功降落至平台指令发送成功！");
                //TODO 4.消息收集到es
                break;
            case 6006:
                logger.info("6006：无人机上传图片通知");
                //1.保存图片
                String imageName = mcsParam.getImageUrl().substring(mcsParam.getImageUrl().lastIndexOf(FORWARD_SLASH) + 1);
                if (!fileService.save(File.builder().relUuid(mcsParam.getHistoryBizId()).filePath(mcsParam.getImageUrl()).fileName(imageName.substring(0, imageName.lastIndexOf(DECIMAL_POINT)))
                        .fileType(imageName.substring(imageName.lastIndexOf(DECIMAL_POINT) + 1)).uploadUserId(null == sessionId ? 1L : sessionId)
                        .uploadTime(LocalDateTime.now()).build()))
                    throw new ServiceException("无人机上传图片保存失败！");
                //2.流程通知
                nettyHandler.sendMessageToAll("无人机上传图片保存到平台成功！");
                //TODO 3.消息收集到es
                break;
            case 6005:
                logger.info("6006：无人机飞行任务结束通知");
                //1.通知机库进行机场回收工作
                if (!FlyTaskUtil.sendMessageToHangar(hangarNumber, 2006, hangarNumber, null == sessionId ? 1L : sessionId, null))
                    throw new ServiceException("通知机库进行机场回收工作指令失败！");
                //2.流程通知
                nettyHandler.sendMessageToAll("通知机库进行机场回收工作指令成功！");
                //TODO 3.消息收集到es
                break;
            default:
                logger.error("未知的MCS指令！");
                break;
        }
    }

    @Override
    public String getType() {
        return "/sys/process/";
    }
}
