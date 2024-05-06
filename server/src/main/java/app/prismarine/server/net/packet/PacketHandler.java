package app.prismarine.server.net.packet;

import app.prismarine.server.net.Connection;

public interface PacketHandler<T extends Packet> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet The packet received by the server to handle
	 */
	void handle(Connection connection, T packet);
}
