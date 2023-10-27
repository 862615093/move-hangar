package snegrid.move.hangar.netty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * netty 配置文件
 *
 * @author wangwei
 */
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {

	/**
	 * 端口
	 */
	private int port;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
