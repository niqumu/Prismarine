package app.prismarine.api;

/**
 * Represents a Minecraft server
 */
public interface Server {

	/**
	 * Gets the name of the server implementation
	 * @return The server implementation name
	 */
	String getName();

	/**
	 * Gets the Minecraft client version the server is targeted towards
	 * @return The Minecraft version the server supports
	 */
	String getVersion();

	/**
	 * Gets the protocol version the server is running on
	 * @return The protocol version the server supports
	 */
	int getProtocol();

	/**
	 * Gets the IP address the server is operating on
	 * @return The IP address of the server
	 */
	String getIP();

	/**
	 * Gets the port the server is operating on
	 * @return The port of the server
	 */
	int getPort();

}
