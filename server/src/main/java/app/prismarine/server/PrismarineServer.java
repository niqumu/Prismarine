package app.prismarine.server;

import app.prismarine.server.net.NettyServer;
import app.prismarine.server.net.packet.PacketManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class of the Prismarine server
 */
public class PrismarineServer {

	/**
	 * The game and protocol version the server is targeting
	 */
	public static final String GAME_VERSION = "1.20.6";
	public static final int PROTOCOL_VERSION = 766;

	/**
	 * The global static server instance
	 */
	@Getter
	private static PrismarineServer server;

	/**
	 * The server's logger
	 */
	@Getter
	private final Logger logger = LoggerFactory.getLogger(PrismarineServer.class);

	/**
	 * Whether the server is currently alive or not
	 */
	@Getter
	private boolean running = false;

	/**
	 * The server's {@link NettyServer} for handling networking and connections
	 */
	@Getter
	private NettyServer nettyServer;

	/**
	 * The server's {@link PrismarineConfig} instance
	 */
	@Getter
	private PrismarineConfig config;

	@SneakyThrows
	public void startup() {

		// Ensure that the server hasn't already been started
		if (this.isRunning()) {
			throw new IllegalStateException("The server is already running!");
		}

		final long startTime = System.currentTimeMillis();
		logger.info("Starting Prismarine!");

		// Set up the shutdown hooks
		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

		// Read the configuration
		this.config = new PrismarineConfig();

		// Start the netty server
		logger.info("Starting server at {}:{}", this.config.getIp(), this.config.getPort());
		this.nettyServer = new NettyServer(this);
		this.nettyServer.startup();

		PacketManager.registerPackets();

		this.running = true;
		logger.info("Done! Started Prismarine in {} ms", System.currentTimeMillis() - startTime);
	}

	public void shutdown() {
		logger.info("Stopping!");
		this.config.save();
		this.nettyServer.shutdown();
	}

	public static void main(String[] args) {
		server = new PrismarineServer();
		server.startup();
	}
}
