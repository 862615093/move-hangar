package snegrid.move.hangar.business.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snegrid.move.hangar.base.AjaxResult;
import snegrid.move.hangar.business.domain.dto.FlyTaskDTO;
import snegrid.move.hangar.business.domain.dto.FlyTaskPicListDTO;
import snegrid.move.hangar.business.domain.entity.Device;
import snegrid.move.hangar.business.domain.entity.File;
import snegrid.move.hangar.business.domain.entity.FlyTask;
import snegrid.move.hangar.business.domain.entity.Route;
import snegrid.move.hangar.business.enums.OrderMuster;
import snegrid.move.hangar.business.mapper.FlyTaskMapper;
import snegrid.move.hangar.business.service.*;
import snegrid.move.hangar.exception.ServiceException;
import snegrid.move.hangar.netty.message.Message;
import snegrid.move.hangar.netty.message.MessageUtil;
import snegrid.move.hangar.netty.model.User;
import snegrid.move.hangar.security.util.SecurityUtils;
import snegrid.move.hangar.utils.fly.FlyTaskUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 飞行任务表 服务实现类
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@Service
@AllArgsConstructor
public class FlyTaskServiceImpl extends ServiceImpl<FlyTaskMapper, FlyTask> implements IFlyTaskService {

    private static final Logger logger = LoggerFactory.getLogger(FlyTaskServiceImpl.class);

    private final FlyTaskMapper flyTaskMapper;

    private final IRouteService routeService;

    private final IDeviceService deviceService;

    private final IFileService fileService;

    private final L1CacheService l1CacheService;

    @Override
    public Message droneControl(User user, Integer msgType) {
        //        String droneNumber = String.valueOf(l1CacheService.get("DroneNumber"));
        String droneNumber = "1581F6GKB238400400B4";
        return FlyTaskUtil.sendControlMessage(droneNumber, msgType, droneNumber, user, null);
    }

    @Override
    public Message hangarControl(User user, Integer msgType) {
        //        String hangarNumber = String.valueOf(l1CacheService.get("HangarNumber"));
        String hangarNumber = "SNE00001";
        return FlyTaskUtil.sendControlMessage(hangarNumber, msgType, hangarNumber, user, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message startTask(User user, String routeId) {
        logger.info("startTask()方法入参： user=【{}】, routeId=【{}】", user, routeId);
        try {
            Device hangarDevice = deviceService.getById(routeService.getById(routeId).getHangarId());
            if (ObjectUtil.isNull(hangarDevice))
                return MessageUtil.getMessage(user, false, "航线ID=" + routeId + "关联设备未找到!");
            //1.飞前条件校验
//            Map<String, Object> preFlyCheck = FlyTaskUtil.preFlyCheck(hangarDevice.getDeviceNumber());
//            if (!MapUtils.getBoolean(preFlyCheck, FLAG))
//                return MessageUtil.getMessage(user, false, MapUtils.getString(preFlyCheck, MSG));
            //2.创建飞行任务
            if (!this.save(FlyTask.builder()
                    .routeId(Long.parseLong(routeId))
                    .hangarId(hangarDevice.getId())
                    .droneId(hangarDevice.getMatchId())
                    .taskStartTime(LocalDateTime.now())
                    .createBy(user.getUserId())
                    .createTime(LocalDateTime.now())
                    .build())) return MessageUtil.getMessage(user, false, "创建飞行任务失败！");
            //3.发送机库准备指令
            if (!FlyTaskUtil.sendMessageToHangar(hangarDevice.getDeviceNumber(), 2000, hangarDevice.getDeviceNumber(), user.getUserId(), null))
                return MessageUtil.getMessage(user, false, "发送机库准备指令失败！");
        } catch (Exception e) {
            logger.error("开始飞行startTask()方法异常", e);
            return MessageUtil.getMessage(user, false, e.getMessage());
        }
        return MessageUtil.getMessage(user, true, "开始任务指令下发成功！");
    }

    @Override
    public AjaxResult startTaskForPublic(FlyTaskDTO dto) {
        FlyTask flyTask = new FlyTask();
        try {
            Route route = routeService.getById(dto.getRouteId());
            if (ObjectUtil.isNull(route)) return AjaxResult.error("航线ID=" + dto.getRouteId() + "不存在!");
            Device hangarDevice = deviceService.getById(route.getHangarId());
            if (ObjectUtil.isNull(hangarDevice))
                return AjaxResult.error("航线ID=" + dto.getRouteId() + "关联设备未找到!");
            //1.飞前条件校验
//            Map<String, Object> preFlyCheck = FlyTaskUtil.preFlyCheck(hangarDevice.getDeviceNumber());
//            if (!MapUtils.getBoolean(preFlyCheck, FLAG)) return AjaxResult.error(MapUtils.getString(preFlyCheck, MSG));
            //2.创建飞行任务
            flyTask = FlyTask.builder()
                    .routeId(dto.getRouteId())
                    .hangarId(hangarDevice.getId())
                    .droneId(hangarDevice.getMatchId())
                    .taskStartTime(LocalDateTime.now())
                    .createBy(SecurityUtils.getUserId())
                    .createTime(LocalDateTime.now())
                    .build();
            if (!this.save(flyTask)) return AjaxResult.error("机库管控平台：创建飞行任务失败！");
            //3.发送机库准备指令
            if (!FlyTaskUtil.sendMessageToHangar(hangarDevice.getDeviceNumber(), 2000, hangarDevice.getDeviceNumber(), SecurityUtils.getUserId(), null))
                return AjaxResult.error("机库管控平台：发送机库准备指令失败！");
        } catch (Exception e) {
            logger.error("startTaskForPublic()方法异常:{}", e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success(flyTask);
    }

    @Override
    public FlyTask getLastFlyTask() {
        List<FlyTask> flyTaskList = flyTaskMapper.selectList(new LambdaQueryWrapper<FlyTask>().eq(FlyTask::getValid, true).orderByDesc(FlyTask::getCreateTime).last("LIMIT 1"));
        Optional.ofNullable(flyTaskList.get(0)).orElseThrow(() -> new ServiceException("当前飞行任务不存在！"));
        return flyTaskList.get(0);
    }

    @Override
    public ArrayList<String> flyTaskPicListForPublic(FlyTaskPicListDTO dto) {
        return (ArrayList<String>) fileService.getAttachmentFileList(dto.getFlyTaskId()).stream().map(File::getFileUrl).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("all")
    public Map<String, List> getTaskOrder() {
        Map<String, List> resMap = new LinkedHashMap<>();
        for (OrderMuster s : EnumSet.allOf(OrderMuster.class)) {
            if (null == resMap.get(s.getCmdType())) {
                List<Map> orderList = new ArrayList<>();
                Map orderMap = new HashMap(4);
                orderMap.put("msgType", s.getMsgType());
                orderMap.put("msgName", s.getFunction());
                orderMap.put("modelType", s.getModelType());
                orderMap.put("deviceType", s.getDeviceType());
                orderList.add(orderMap);
                resMap.put(s.getCmdType(), orderList);
            } else {
                List<Map> orderList = resMap.get(s.getCmdType());
                Map orderMap = new HashMap(4);
                orderMap.put("msgType", s.getMsgType());
                orderMap.put("msgName", s.getFunction());
                orderMap.put("modelType", s.getModelType());
                orderMap.put("cmdType", s.getCmdType());
                orderMap.put("deviceType", s.getDeviceType());
                orderList.add(orderMap);
                resMap.put(s.getCmdType(), orderList);
            }
        }
        return resMap;
    }
}
