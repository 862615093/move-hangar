package snegrid.move.hangar.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import snegrid.move.hangar.netty.config.NettyConfig;

import javax.annotation.PreDestroy;

/**
 * websocket服务器
 *
 * @author wangwei
 */
@Component
@AllArgsConstructor
public class NettyWebsocketServer implements Runnable {

    private final NettyConfig nettyConfig;

    private final EventLoopGroup boss = new NioEventLoopGroup();

    private final EventLoopGroup work = new NioEventLoopGroup();

    @PreDestroy
    public void close() {
        boss.shutdownGracefully();
        work.shutdownGracefully();
    }

    @Override
    public void run() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new WebSocketChannelInitializer());
        ChannelFuture channelFuture;
        try {
            channelFuture = bootstrap.bind(nettyConfig.getPort()).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
