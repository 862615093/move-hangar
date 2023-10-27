package snegrid.move.hangar.netty.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import snegrid.move.hangar.netty.handler.NettyUserManager;
import snegrid.move.hangar.netty.model.User;
import snegrid.move.hangar.utils.spring.SpringUtils;

import static snegrid.move.hangar.constant.MagicValue.MSG;

/**
 * 获取netty消息
 *
 * @author wangwei
 */
@Component
public class MessageUtil {

    private static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    private static final NettyUserManager nettyUserManager = SpringUtils.getBean(NettyUserManager.class);

    /**
     * 获取 message
     *
     * @param user   当前连接用户
     * @param result 结果标识
     * @param msg    消息提示
     * @return 结果
     */
    public static Message getMessage(User user, boolean result, String msg) {
        CommonMessage commonMessage;
        if (result) {
            commonMessage = new CommonMessage(MessageType.SUCCESS, user);
            commonMessage.put(MSG, msg);
        } else {
            commonMessage = new CommonMessage(MessageType.ERROR, user);
            commonMessage.put(MSG, msg);
        }
        return commonMessage;
    }
}
