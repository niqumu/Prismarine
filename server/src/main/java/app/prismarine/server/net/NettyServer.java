package app.prismarine.server.net;

import app.prismarine.server.PrismarineServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NettyServer {

	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();

	@Getter
	private boolean running = false;

	@Getter
	private final PrismarineServer server;

	@Getter
	private final Set<Connection> connections = ConcurrentHashMap.newKeySet();

	public NettyServer(PrismarineServer server) {
		this.server = server;
	}

	public void startup() {
		if (this.running) {
			throw new IllegalStateException("The NettyServer is already running!");
		}

		ServerBootstrap bootstrap = new ServerBootstrap();

		bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new NettyChannelInitializer(this))
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE, true);

		// Bind the server and update the state accordingly
		bootstrap.bind(this.server.getIp(), this.server.getPort()).addListener(future ->
			this.running = future.isSuccess());
	}

	public void shutdown() {
		if (this.running) {
			throw new IllegalStateException("The NettyServer is not running!");
		}

		this.workerGroup.shutdownGracefully();
		this.bossGroup.shutdownGracefully();

		this.running = true;
	}

}
