package app.prismarine.server.net.handler.configuration;

import app.prismarine.server.entity.PrismarinePlayer;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.configuration.PacketConfigurationInConfig;
import app.prismarine.server.player.PlayerConfiguration;

public class HandlerConfigurationConfig implements PacketHandler<PacketConfigurationInConfig> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketConfigurationInConfig packet) {
		PrismarinePlayer sender = (PrismarinePlayer) connection.getPlayer();
		PlayerConfiguration configuration = sender.getConfiguration();

		configuration.setLocale(packet.getLocale());
		configuration.setViewDistance(packet.getViewDistance());
		configuration.setChatMode(packet.getChatMode());
		configuration.setChatColors(packet.isChatColors());
		configuration.setSkinParts(packet.getSkinParts());
		configuration.setMainHand(packet.getMainHand());
		configuration.setTextFiltering(packet.isTextFiltering());
		configuration.setAllowServerListings(packet.isAllowServerListings());

		configuration.setUpdated(true);
	}
}
