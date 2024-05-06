package app.prismarine.server;

import app.prismarine.server.net.NettyServer;
import app.prismarine.server.net.packet.PacketManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrismarineServer {

	public static final String GAME_VERSION = "Prismarine - 1.20.6";
	public static final int PROTOCOL_VERSION = 766;

	@Getter
	private static PrismarineServer server;

	@Getter
	private final Logger logger = LoggerFactory.getLogger(PrismarineServer.class);

	@Getter
	private final String ip = "0.0.0.0";
	@Getter
	private final int port = 25565;

	@Getter
	private boolean running = false;

	@Getter
	private NettyServer nettyServer;

	@SneakyThrows
	public void startup() {

		// Ensure that the server hasn't already been started
		if (this.isRunning()) {
			throw new IllegalStateException("The server is already running!");
		}

		final long startTime = System.currentTimeMillis();
		logger.info("Starting Prismarine at {}:{}", this.ip, this.port);


		this.nettyServer = new NettyServer(this);
		this.nettyServer.startup();

		PacketManager.registerPackets();

		this.running = true;
		logger.info("Done! Started Prismarine in {} ms", System.currentTimeMillis() - startTime);
	}

	public void shutdown() {
		this.nettyServer.shutdown();
	}

	public static void main(String[] args) {
		server = new PrismarineServer();
		server.startup();
	}
}
