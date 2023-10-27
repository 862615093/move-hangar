package snegrid.move.hangar.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import snegrid.move.hangar.base.AjaxResult;
import snegrid.move.hangar.business.domain.dto.FlyTaskDTO;
import snegrid.move.hangar.business.domain.dto.FlyTaskPicListDTO;
import snegrid.move.hangar.business.domain.entity.FlyTask;
import snegrid.move.hangar.netty.message.Message;
import snegrid.move.hangar.netty.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 飞行任务表 服务类
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
public interface IFlyTaskService extends IService<FlyTask> {

    Map<String, List> getTaskOrder();

    /**
     * 开始飞行任务
     *
     * @param user    当前连接用户
     * @param routeId 航线ID
     * @return Message
     */
    Message startTask(User user, String routeId);

    /**
     * 获取当前飞行任务
     *
     * @return
     */
    FlyTask getLastFlyTask();

    /**
     * 无人机控制指令直接发送mqtt
     *
     * @param user    当前连接用户
     * @param msgType 指令
     * @return 结果
     */
    Message droneControl(User user, Integer msgType);

    /**
     * 无机库控制指令直接发送mqtt
     *
     * @param user    当前连接用户
     * @param msgType 指令
     * @return 结果
     */
    Message hangarControl(User user, Integer msgType);

    /**
     * 开放飞行任务接口
     *
     * @param dto 入参
     * @return 结果
     */
    AjaxResult startTaskForPublic(FlyTaskDTO dto);

    /**
     * 获取飞行任务航拍图
     *
     * @param dto 入参
     * @return 结果
     */
    ArrayList<String> flyTaskPicListForPublic(FlyTaskPicListDTO dto);
}
