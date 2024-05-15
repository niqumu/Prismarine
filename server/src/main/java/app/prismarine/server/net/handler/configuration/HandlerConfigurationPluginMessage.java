package app.prismarine.server.net.handler.configuration;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.configuration.PacketConfigurationInPluginMessage;
import app.prismarine.server.net.packet.configuration.PacketConfigurationOutPluginMessage;
import org.bukkit.Bukkit;

public class HandlerConfigurationPluginMessage implements PacketHandler<PacketConfigurationInPluginMessage> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketConfigurationInPluginMessage packet) {

		// Respond to the client brand message with the server brand
		if (packet.getChannel().equals("minecraft:brand")) {
			connection.sendPacket(new PacketConfigurationOutPluginMessage(
				"minecraft:brand", Bukkit.getServer().getName()));
		}
	}
}
