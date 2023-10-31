package snegrid.move.hangar.netty.handler;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import snegrid.move.hangar.business.service.IFlyTaskService;
import snegrid.move.hangar.netty.message.CommonMessage;
import snegrid.move.hangar.netty.message.Message;
import snegrid.move.hangar.netty.message.MessageType;
import snegrid.move.hangar.netty.message.MessageUtil;
import snegrid.move.hangar.netty.model.User;
import snegrid.move.hangar.utils.common.StringUtils;

import java.util.List;
import java.util.Map;

import static snegrid.move.hangar.constant.MagicValue.MSG;
import static snegrid.move.hangar.constant.MagicValue.MSG_TYPE;

/**
 * handler 处理器
 *
 * @author wangwei
 */
@Component
@AllArgsConstructor
public class CommonNettyHandler implements NettyHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommonNettyHandler.class);

    private final NettyUserManager nettyUserManager;

    private final IFlyTaskService flyTaskService;

    @Handle(MessageType.LONGIN)
    public Message userLogin(String channelId, String userName, Long userId) {
        logger.info("6.userLogin()... channelId={}, userName={}, userId={}", channelId, userName, userId);
        User user = new User(channelId, userName, userId);
        nettyUserManager.addUser(user);
        CommonMessage commonMessage = new CommonMessage(MessageType.SUCCESS, user);
        commonMessage.put("msg", "登录成功!");
        return commonMessage;
    }

    @Handle(MessageType.STAR_FLY_TASK)
    public Message startFlyTask(String channelId, String routeId) {
        logger.info("6.startFlyTask()... channelId={}, routeId={}", channelId, routeId);
        Map<String, User> userMap = nettyUserManager.getUserMap();
        User user = userMap.get(channelId);
        Message message = null;
        try {
            message = flyTaskService.startFlyTask(user, routeId);
        } catch (Exception e) {
            logger.error("开始任务指令下发异常", e);
            message = MessageUtil.getMessage(user, false, "指令下发失败！");
        }
        logger.info("6.1.message={}", message);
        return message;
    }

    @Handle(MessageType.END_FLY_TASK)
    public Message endFlyTask(String channelId) {
        logger.info("6.endFlyTask()... channelId={}", channelId);
        Map<String, User> userMap = nettyUserManager.getUserMap();
        User user = userMap.get(channelId);
        Message message = null;
        try {
            message = flyTaskService.endFlyTask(user);
        } catch (Exception e) {
            logger.error("开始任务指令下发异常", e);
            message = MessageUtil.getMessage(user, false, "指令下发失败！");
        }
        logger.info("6.1.message={}", message);
        return message;
    }

    @Handle(MessageType.DRONE_CONTROL)
    public Message droneControl(String channelId, Integer msgType) {
        logger.info("6.droneControl()... channelId={}, msgType={}", channelId, msgType);
        Map<String, User> userMap = nettyUserManager.getUserMap();
        User user = userMap.get(channelId);
        Message message = null;
        try {
            message = flyTaskService.droneControl(user, msgType);
        } catch (Exception e) {
            logger.error("无人机控制指令下发异常", e);
            message = MessageUtil.getMessage(user, false, "指令下发失败！");
        }
        logger.info("6.1.message={}", message);
        return message;
    }

    @Handle(MessageType.HANGAR_CONTROL)
    public Message hangarControl(String channelId, Integer msgType) {
        logger.info("6.hangarControl()... channelId={}, msgType={}", channelId, msgType);
        Map<String, User> userMap = nettyUserManager.getUserMap();
        User user = userMap.get(channelId);
        Message message = null;
        try {
            message = flyTaskService.hangarControl(user, msgType);
        } catch (Exception e) {
            logger.error("机库控制指令下发异常", e);
            message = MessageUtil.getMessage(user, false, "指令下发失败！");
        }
        logger.info("6.1.message={}", message);
        return message;
    }

    public void sendMessageToOne(Long userId, String msg) {
        List<User> allUser = nettyUserManager.getAllUser();
        if (StringUtils.isEmpty(msg) || CollUtil.isEmpty(allUser)) {
            return;
        }
        //遍历获取指定用户发送消息
        for (User user : allUser) {
            if (user.getUserId().equals(userId)) {
                CommonMessage message = new CommonMessage(MessageType.SUCCESS, user).put(MSG, msg);
                user.handleRequest(message);
                return;
            }
        }
    }

    public void sendMessageToAll(String msg) {
        List<User> allUser = nettyUserManager.getAllUser();
        if (StringUtils.isEmpty(msg) || CollUtil.isEmpty(allUser)) {
            return;
        }
        allUser.stream().parallel().forEach(user -> {
            CommonMessage message = new CommonMessage(MessageType.SUCCESS, user)
                    .put(MSG, msg);
            user.handleRequest(message);
        });
    }

    public void sendMessageToAll(String msgType, String msg) {
        List<User> allUser = nettyUserManager.getAllUser();
        if (StringUtils.isEmpty(msg) || CollUtil.isEmpty(allUser)) {
            return;
        }
        allUser.stream().parallel().forEach(user -> {
            CommonMessage message = new CommonMessage(MessageType.SUCCESS, user)
                    .put(MSG_TYPE, msgType)
                    .put(MSG, msg);
            user.handleRequest(message);
        });
    }
}