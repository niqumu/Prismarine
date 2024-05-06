package app.prismarine.server.net.handler.status;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.status.PacketStatusInPingRequest;
import app.prismarine.server.net.packet.status.PacketStatusOutPingResponse;

public class HandlerStatusPingRequest implements PacketHandler<PacketStatusInPingRequest> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketStatusInPingRequest packet) {
		connection.sendPacket(new PacketStatusOutPingResponse(packet.getPayload()));
	}
}
