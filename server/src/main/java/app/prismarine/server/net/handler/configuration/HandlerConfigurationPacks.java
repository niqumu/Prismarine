package app.prismarine.server.net.handler.configuration;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.configuration.PacketConfigurationInPacks;
import app.prismarine.server.net.packet.configuration.PacketConfigurationOutFinish;

public class HandlerConfigurationPacks implements PacketHandler<PacketConfigurationInPacks> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketConfigurationInPacks packet) {
		connection.sendPacket(new PacketConfigurationOutFinish());
	}
}
