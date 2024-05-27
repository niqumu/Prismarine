package app.prismarine.server.net.handler.configuration;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.configuration.PacketConfigurationInAcknowledgeFinish;
import app.prismarine.server.net.packet.play.out.*;
import app.prismarine.server.world.PrismarineChunk;
import org.bukkit.Bukkit;

public class HandlerConfigurationAcknowledgeFinish implements PacketHandler<PacketConfigurationInAcknowledgeFinish> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketConfigurationInAcknowledgeFinish packet) {
		connection.setState(ConnectionState.PLAY);

		// Send login packet
		connection.sendPacket(new PacketPlayOutLogin(connection.getPlayer()));

		// Set the player position
		connection.sendPacket(new PacketPlayOutSyncPlayerPosition(0, 64, 0, 0, 0));

		// Send player info
		connection.sendPacket(new PacketPlayOutPlayerInfoUpdate(connection.getPlayer().getUniqueId(),
			new PacketPlayOutPlayerInfoUpdate.ActionAddPlayer(connection.getPlayer().getName())));

		// Prepare for the player's world to send chunks
		connection.sendPacket(new PacketPlayOutGameEvent(PacketPlayOutGameEvent.Event.START_WAITING_FOR_CHUNKS, 0));

		// TODO testing
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				connection.sendPacket(new PacketPlayOutChunkData(new PrismarineChunk(x, z, null)));
			}
		}
	}
}
