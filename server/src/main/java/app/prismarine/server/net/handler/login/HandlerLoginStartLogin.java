package app.prismarine.server.net.handler.login;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.login.PacketLoginInLoginStart;
import app.prismarine.server.net.packet.login.PacketLoginOutSuccess;

public class HandlerLoginStartLogin implements PacketHandler<PacketLoginInLoginStart> {
	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketLoginInLoginStart packet) {
		connection.sendPacket(new PacketLoginOutSuccess(packet.getUuid(), packet.getName()));
	}
}
