package app.prismarine.server.net;

/**
 * An enum of all connection states that exist between the Minecraft client and server.
 * <p>
 * Different connection states provide different packet mappings and specifications. For example, a packet with an
 * ID of 0x03 cannot be identified without knowing the connection state, as each connection state has a different
 * packet with the ID of 0x03.
 */
public enum ConnectionState {

	/**
	 * The initial state of all connections opened between Minecraft and the server
	 * <p>
	 * In {@link app.prismarine.server.net.packet.handshake.PacketHandshakeInHandshake}, the client specifies
	 * if they wish to set the state to STATUS or to LOGIN, depending on the client's intentions
	 */
	HANDSHAKING,

	/**
	 * Used by the Minecraft client to request information for the multiplayer server list
	 */
	STATUS,

	/**
	 * Set by the Minecraft client when it wishes to connect to a server with the intention of playing on it.
	 * This state includes authentication.
	 */
	LOGIN,

	/**
	 * If the login process succeeds, this state allows the client and server to exchange information related to
	 * their configurations, including brand messages and resource packs
	 */
	CONFIGURATION,

	/**
	 * The connection state that exists when the client is completely connected to and playing on a server
	 */
	PLAY
}
