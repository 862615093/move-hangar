package snegrid.move.hangar;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import snegrid.move.hangar.netty.handler.HandlerRegister;
import snegrid.move.hangar.netty.handler.NettyHandler;
import snegrid.move.hangar.netty.server.NettyWebsocketServer;
import snegrid.move.hangar.utils.spring.SpringUtils;

import java.util.Arrays;
/**
 * 启动类
 *
 * @author wangwei
 */
@EnableAsync
@AllArgsConstructor
@SpringBootApplication
@EnableTransactionManagement
public class MoveHangarApplication implements CommandLineRunner {

    private final NettyWebsocketServer nettyWebsocketServer;

    public static void main(String[] args) {
        SpringUtils.applicationContext = SpringApplication.run(MoveHangarApplication.class, args);
        System.out.println("1.注册消息处理...");
        Arrays.stream(SpringUtils.applicationContext.getBeanNamesForType(NettyHandler.class))
                .forEach(beanName -> {
                    Object object = SpringUtils.applicationContext.getBean(beanName);
                    if (object instanceof NettyHandler) {
                        try {
                            HandlerRegister.register((NettyHandler) object);
                        } catch (Exception e) {
                            throw new RuntimeException("netty handler注册失败...");
                        }
                    }
                });
        System.out.println("(♥◠‿◠)ﾉﾞ  搞得真不丑,移动机库管控平台启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }

    @Override
    public void run(String... args) {
        //启动 netty websocket 服务器
        new Thread(nettyWebsocketServer).start();
    }
}