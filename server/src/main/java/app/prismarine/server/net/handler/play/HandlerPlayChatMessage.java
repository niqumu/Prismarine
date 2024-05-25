package app.prismarine.server.net.handler.play;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.play.in.PacketPlayInChatMessage;

public class HandlerPlayChatMessage implements PacketHandler<PacketPlayInChatMessage> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketPlayInChatMessage packet) {
		if (packet.getMessage().isEmpty()) {
			return;
		}

		if (packet.getMessage().length() > 256) {
			connection.disconnect("Message length (" + packet.getMessage().length() + ") must" +
				"be less than 256!");
			return;
		}

		connection.getPlayer().chat(packet.getMessage());
	}
}
