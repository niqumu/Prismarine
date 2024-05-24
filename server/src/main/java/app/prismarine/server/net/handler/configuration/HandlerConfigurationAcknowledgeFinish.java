package app.prismarine.server.net.handler.configuration;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.configuration.PacketConfigurationInAcknowledgeFinish;
import app.prismarine.server.net.packet.play.out.*;

public class HandlerConfigurationAcknowledgeFinish implements PacketHandler<PacketConfigurationInAcknowledgeFinish> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketConfigurationInAcknowledgeFinish packet) {
		connection.setState(ConnectionState.PLAY);

		connection.sendPacket(new PacketPlayOutLogin(connection.getPlayer()));
		connection.sendPacket(new PacketPlayOutSyncPlayerPosition(0, 64, 0, 0, 0));

		connection.sendPacket(new PacketPlayOutPlayerInfoUpdate(connection.getPlayer().getUniqueId(),
			new PacketPlayOutPlayerInfoUpdate.ActionAddPlayer(connection.getPlayer().getName())));

		connection.sendPacket(new PacketPlayOutGameEvent(PacketPlayOutGameEvent.Event.START_WAITING_FOR_CHUNKS, 0));

		connection.sendPacket(new PacketPlayOutSetCenterChunk(0, 0));

		// Send chunks
		connection.sendPacket(new PacketPlayOutChunkData(-1, -1));
		connection.sendPacket(new PacketPlayOutChunkData(-1, 0));
		connection.sendPacket(new PacketPlayOutChunkData(-1, 1));
		connection.sendPacket(new PacketPlayOutChunkData(0, -1));
		connection.sendPacket(new PacketPlayOutChunkData(0, 0));
		connection.sendPacket(new PacketPlayOutChunkData(0, 1));
		connection.sendPacket(new PacketPlayOutChunkData(1, -1));
		connection.sendPacket(new PacketPlayOutChunkData(1, 0));
		connection.sendPacket(new PacketPlayOutChunkData(1, 1));
	}
}
