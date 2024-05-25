package app.prismarine.server.net.handler.play;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.play.in.PacketPlayInKeepAlive;

public class HandlerPlayKeepAlive implements PacketHandler<PacketPlayInKeepAlive> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketPlayInKeepAlive packet) {
		long latency = System.currentTimeMillis() - packet.getPayload();
		connection.setLatency((int) latency);
	}
}
