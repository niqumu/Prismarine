package app.prismarine.server.net;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.net.packet.Packet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import org.bukkit.Bukkit;

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
			.childOption(ChannelOption.TCP_NODELAY, true)
			.childOption(ChannelOption.SO_KEEPALIVE, true);

		// Bind the server and update the state
		bootstrap.bind(this.server.getIp(), this.server.getPort()).
			addListener(future -> {

				this.running = true;

				// If binding the server failed
				if (!future.isSuccess()) {
					this.running = false;
					PrismarineServer.LOGGER.error("Failed to bind the server to {}:{}: {}",
						this.server.getIp(), this.server.getPort(), future.cause().toString());

					System.exit(1);
				}
			});
	}

	public void shutdown() {
		if (!this.running) {
			throw new IllegalStateException("The NettyServer is not running!");
		}

		// Close all connections, disconnecting all players with the shutdown message
		this.connections.forEach(connection -> connection.disconnect(server.getShutdownMessage()));

		this.workerGroup.shutdownGracefully();
		this.bossGroup.shutdownGracefully();

		this.running = false;
	}

	/**
	 * Broadcast a packet to all currently online players
	 * @param packet The packet to broadcast
	 */
	public void broadcastPacket(Packet packet) {
		this.connections.forEach(connection -> {
			if (connection.getState().equals(ConnectionState.PLAY)) {
				connection.sendPacket(packet);
			}
		});
	}

}
