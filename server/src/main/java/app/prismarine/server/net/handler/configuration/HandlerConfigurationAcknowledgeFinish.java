package app.prismarine.server.net.handler.configuration;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.configuration.PacketConfigurationInAcknowledgeFinish;
import app.prismarine.server.net.packet.play.out.PacketPlayOutLogin;

public class HandlerConfigurationAcknowledgeFinish implements PacketHandler<PacketConfigurationInAcknowledgeFinish> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketConfigurationInAcknowledgeFinish packet) {
		connection.setState(ConnectionState.PLAY);

		connection.sendPacket(new PacketPlayOutLogin(connection.getPlayer()));
	}
}
