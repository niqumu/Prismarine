package app.prismarine.server;

import app.prismarine.api.Server;
import app.prismarine.server.net.NettyServer;
import app.prismarine.server.net.packet.PacketManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class of the Prismarine server
 */
public class PrismarineServer implements Server {

	/**
	 * The game and protocol version the server is targeting
	 */
	public static final String GAME_VERSION = "1.20.6";
	public static final int PROTOCOL_VERSION = 766;

	/**
	 * The server's logger
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(PrismarineServer.class);

	/**
	 * The global static server instance
	 */
	@Getter
	private static PrismarineServer server;

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

	/**
	 * Starts up the server
	 */
	@SneakyThrows
	public void startup() {

		// Ensure that the server hasn't already been started
		if (this.isRunning()) {
			throw new IllegalStateException("The server is already running!");
		}

		final long startTime = System.currentTimeMillis();
		LOGGER.info("Starting Prismarine!");

		// Set up the shutdown hooks
		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

		// Read the configuration
		this.config = new PrismarineConfig();

		// Start the netty server
		LOGGER.info("Starting server at {}:{}", this.config.getIp(), this.config.getPort());
		this.nettyServer = new NettyServer(this);
		this.nettyServer.startup();

		PacketManager.registerPackets();

		this.running = true;
		LOGGER.info("Done! Started Prismarine in {} ms", System.currentTimeMillis() - startTime);
	}

	/**
	 * Cleans up the server on shutdown. This method is registered as a shutdown hook on server startup
	 */
	public void shutdown() {
		Thread.currentThread().setName("Shutdown-Thread");

		LOGGER.info("Stopping!");
		this.config.save();
		this.nettyServer.shutdown();
	}

	public static void main(String[] args) {
		server = new PrismarineServer();
		server.startup();
	}

	/**
	 * Gets the name of the server implementation
	 *
	 * @return The server implementation name
	 */
	@Override
	public String getName() {
		return "Prismarine";
	}

	/**
	 * Gets the Minecraft client version the server is targeted towards
	 *
	 * @return The Minecraft version the server supports
	 */
	@Override
	public String getVersion() {
		return GAME_VERSION;
	}

	/**
	 * Gets the protocol version the server is running on
	 *
	 * @return The protocol version the server supports
	 */
	@Override
	public int getProtocol() {
		return PROTOCOL_VERSION;
	}

	/**
	 * Gets the IP address the server is operating on
	 *
	 * @return The IP address of the server
	 */
	@Override
	public String getIP() {
		return this.config.getIp();
	}

	/**
	 * Gets the port the server is operating on
	 *
	 * @return The port of the server
	 */
	@Override
	public int getPort() {
		return this.config.getPort();
	}
}
