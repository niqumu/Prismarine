package app.prismarine.server.net.handler.play;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.play.in.PacketPlayInChatCommand;
import app.prismarine.server.net.packet.play.in.PacketPlayInChatMessage;

public class HandlerPlayChatCommand implements PacketHandler<PacketPlayInChatCommand> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketPlayInChatCommand packet) {
		if (packet.getCommand().isEmpty()) {
			return;
		}

		if (packet.getCommand().length() > 256) {
			connection.disconnect("Command length (" + packet.getCommand().length() + ") must" +
				"be less than 256!");
			return;
		}

		connection.getPlayer().performCommand(packet.getCommand());
	}
}
