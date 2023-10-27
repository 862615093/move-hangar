package snegrid.move.hangar.netty.model;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import snegrid.move.hangar.netty.handler.WebsocketHandler;
import snegrid.move.hangar.netty.message.Message;

/**
 * 消息处理类
 *
 * @author wangwei
 */
public class User extends AbstractEntity {

    private String userName;

    private Long userId;

    public User(String id, String userName, Long userId) {
        this.setId(id);
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public void handleRequest(Message message) {
//        System.out.println("7.handleRequest()--->message=" + new Gson().toJson(message));
        System.out.println("7.handleRequest()--->message=" + JSON.toJSONString(message));
//        WebsocketHandler.channelMap.forEach((k, v) -> {
//            while (true) {
//                System.out.println("k=" + k);
//                System.out.println("v=" + new Gson().toJson(message));
//                v.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(message)));
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
		Channel channel = WebsocketHandler.channelMap.get(this.getId());
		if(channel == null) {
			System.out.println("用户连接通道不存在");
			return;
		}
		channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
    }

    @Override
    public void removeChild(AbstractEntity entity) {
    }
}
