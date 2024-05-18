package app.prismarine.server.net.handler.handshake;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.handshake.PacketHandshakeInHandshake;

public class HandlerHandshake implements PacketHandler<PacketHandshakeInHandshake> {

	/**
	 * @param packet The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketHandshakeInHandshake packet) {
		if (packet.getNextState() == PacketHandshakeInHandshake.HandshakeState.LOGIN) {
			connection.setState(ConnectionState.LOGIN);
			System.out.println("Switching state to LOGIN");

			// On login, ensure the client is compatible with the server
			if (packet.getProtocolVersion() < PrismarineServer.PROTOCOL_VERSION) {
				connection.disconnect("Outdated client! I'm running " +
					PrismarineServer.GAME_VERSION + "!");
				return;
			} else if (packet.getProtocolVersion() > PrismarineServer.PROTOCOL_VERSION) {
				connection.disconnect("Outdated server! I'm running " +
					PrismarineServer.GAME_VERSION + "!");
				return;
			}

			connection.setProtocolVersion(packet.getProtocolVersion());
		} else {
			connection.setState(ConnectionState.STATUS);
			System.out.println("Switching state to STATUS");
		}
	}
}
