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

/**
 * A simple netty server to handle all connections with clients
 */
public class NettyServer {

	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();

	/**
	 * Whether the netty server is currently alive or not
	 */
	@Getter
	private boolean running = false;

	/**
	 * The {@link PrismarineServer} the netty server belongs to
	 */
	@Getter
	private final PrismarineServer server;

	/**
	 * A set of all {@link Connection}s currently connected to the server
	 */
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

		// Bind the server and update the state
		bootstrap.bind(this.server.getConfig().getIp(), this.server.getConfig().getPort()).
			addListener(future -> this.running = future.isSuccess());
	}

	public void shutdown() {
		if (!this.running) {
			throw new IllegalStateException("The NettyServer is not running!");
		}

		this.workerGroup.shutdownGracefully();
		this.bossGroup.shutdownGracefully();

		this.running = false;
	}

}
