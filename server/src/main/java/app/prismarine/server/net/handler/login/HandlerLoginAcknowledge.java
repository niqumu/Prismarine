package app.prismarine.server.net.handler.login;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.login.PacketLoginInAcknowledge;

public class HandlerLoginAcknowledge implements PacketHandler<PacketLoginInAcknowledge> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketLoginInAcknowledge packet) {
		connection.setState(ConnectionState.CONFIGURATION);
		System.out.println("Switching state to CONFIGURATION");
	}
}
