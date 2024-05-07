package app.prismarine.server;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PrismarineConfig {

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
	@Getter
	private final String motd;

	/**
	 * The maximum number of players permitted on the server at one time
	 */
	@Getter
	private final int maxPlayers;

	private final Properties configProperties = new Properties();

	@SneakyThrows
	public PrismarineConfig() {

		File configFile = new File("server.properties");
		boolean configCreated = configFile.createNewFile();

		if (configCreated) {
			PrismarineServer.getServer().getLogger().info("Created config file server.properties");
		}

		configProperties.load(new FileInputStream(configFile));

		this.ip = configProperties.getProperty("ip", "0.0.0.0");
		this.port = Integer.parseInt(configProperties.getProperty("port", "25565"));
		this.motd = configProperties.getProperty("motd", "A Prismarine server");
		this.maxPlayers = Integer.parseInt(configProperties.getProperty("max_players", "20"));
	}

	@SneakyThrows
	public void save() {
		configProperties.setProperty("ip", this.ip);
		configProperties.setProperty("port", String.valueOf(this.port));
		configProperties.setProperty("motd", this.motd);
		configProperties.setProperty("max_players", String.valueOf(this.maxPlayers));

		configProperties.store(new FileOutputStream("server.properties"), null);
	}
}
