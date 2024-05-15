package app.prismarine.server;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ServerConfig {

	/**
	 * The IP address the server is running on
	 */
	@Getter
	private final String ip;

	/**
	 * The port the server is running on
	 */
	@Getter
	private final int port;

	/**
	 * The server's message of the day (MOTD)
	 */
	@Getter @Setter
	private String motd;

	/**
	 * The maximum number of players permitted on the server at one time
	 */
	@Getter @Setter
	private int maxPlayers;

	/**
	 * The amount of world data the server sends the client, measured in chunks in each direction of the player
	 */
	@Getter @Setter
	private int viewDistance;

	/**
	 * The maximum distance from players, in chunks, that living entities may be located in order to be updated by
	 * the server
	 */
	@Getter @Setter
	private int simulationDistance;

	/**
	 * Whether players must be included on the server's whitelist to connect
	 */
	@Getter @Setter
	private boolean whitelist;

	private final Properties configProperties = new Properties();

	@SneakyThrows
	public ServerConfig() {

		File configFile = new File("server.properties");
		boolean configCreated = configFile.createNewFile();

		configProperties.load(new FileInputStream(configFile));

		this.ip = configProperties.getProperty("server-ip", "");
		this.port = Integer.parseInt(configProperties.getProperty("server-port", "25565"));
		this.motd = configProperties.getProperty("motd", "A Prismarine server");
		this.maxPlayers = Integer.parseInt(configProperties.getProperty("max-players", "20"));
		this.simulationDistance = Integer.parseInt(configProperties.getProperty("simulation-distance", "10"));
		this.viewDistance = Integer.parseInt(configProperties.getProperty("view-distance", "10"));
		this.whitelist = Boolean.parseBoolean(configProperties.getProperty("white-list", "false"));

		if (configCreated) {
			PrismarineServer.LOGGER.info("Created config file server.properties");
			this.save();
		}
	}

	@SneakyThrows
	public void save() {
		configProperties.setProperty("server-ip", this.ip);
		configProperties.setProperty("server-port", String.valueOf(this.port));
		configProperties.setProperty("motd", this.motd);
		configProperties.setProperty("max-players", String.valueOf(this.maxPlayers));
		configProperties.setProperty("simulation-distance", String.valueOf(this.simulationDistance));
		configProperties.setProperty("view-distance", String.valueOf(this.viewDistance));
		configProperties.setProperty("white-list", String.valueOf(this.whitelist));

		configProperties.store(new FileOutputStream("server.properties"), "Prismarine server properties");

		PrismarineServer.LOGGER.info("Saved configuration to server.properties");
	}
}
