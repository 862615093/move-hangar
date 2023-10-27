package snegrid.move.hangar.netty.handler;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import snegrid.move.hangar.netty.message.CommonMessage;
import snegrid.move.hangar.netty.message.Message;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务处理器
 *
 * @author wangwei
 */
public class WebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static final Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("4.channelRead0()...");
        Channel channel = ctx.channel();
        CommonMessage commonMessage = new Gson().fromJson(msg.text(), CommonMessage.class);
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("channelId", channel.id().asLongText());
        param.putAll(commonMessage.getParams());
        commonMessage.setParams(param);
        handler(commonMessage);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("3.handlerAdded()...");
        channelMap.putIfAbsent(ctx.channel().id().asLongText(), ctx.channel());
        System.out.println("ChannelId" + ctx.channel().id().asLongText());
    }

    /**
     * 调用协议方法
     * @param commonMessage
     * @return
     * @throws Exception
     * @return String
     * @date 2019年1月9日下午4:03:36
     */
    public void handler(CommonMessage commonMessage) throws Exception {
        System.out.println("5.调用协议方法handler()" + commonMessage);
        Map<String, Object> paramsMap = commonMessage.getParams();
        Object[] params = new Object[paramsMap.size()];
        int i = 0;
        for (Entry<String, Object> entry : paramsMap.entrySet()) {
            params[i] = entry.getValue();
            i++;
        }
        Message message = HandlerRegister.invoke(commonMessage.getCode(), params);
        if (message != null && message.getEntity() != null) {
            message.getEntity().handleRequest(message);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("8.handlerRemoved()...");
        if (channelMap.containsKey(ctx.channel().id().asLongText())) {
            channelMap.remove(ctx.channel().id().asLongText());
        }
//		EventRegist.sendEvent(new Event<String,Object>(EventType.User_Login_Out, ctx.channel().id().asLongText()));
        System.out.println("用户下线: " + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause);
        ctx.channel().close();
    }
}
