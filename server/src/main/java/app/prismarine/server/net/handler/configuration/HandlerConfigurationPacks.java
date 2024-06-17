package app.prismarine.server.net.handler.configuration;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.handler.login.HandlerLoginAcknowledge;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.configuration.PacketConfigurationInPacks;
import app.prismarine.server.net.packet.configuration.PacketConfigurationOutFinish;
import app.prismarine.server.net.packet.configuration.PacketConfigurationOutRegistry;

import java.io.InputStream;

public class HandlerConfigurationPacks implements PacketHandler<PacketConfigurationInPacks> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketConfigurationInPacks packet) {

		// Registry data
		connection.sendPacket(new PacketConfigurationOutRegistry(PacketConfigurationOutRegistry.RegistryType.BIOME));
		connection.sendPacket(new PacketConfigurationOutRegistry(PacketConfigurationOutRegistry.RegistryType.CHAT_TYPE));
		connection.sendPacket(new PacketConfigurationOutRegistry(PacketConfigurationOutRegistry.RegistryType.TRIM_PATTERN));
		connection.sendPacket(new PacketConfigurationOutRegistry(PacketConfigurationOutRegistry.RegistryType.TRIM_MATERIAL));
		connection.sendPacket(new PacketConfigurationOutRegistry(PacketConfigurationOutRegistry.RegistryType.WOLF_VARIANT));
		connection.sendPacket(new PacketConfigurationOutRegistry(PacketConfigurationOutRegistry.RegistryType.PAINTING_VARIANT));
		connection.sendPacket(new PacketConfigurationOutRegistry(PacketConfigurationOutRegistry.RegistryType.DIMENSION_TYPE));
		connection.sendPacket(new PacketConfigurationOutRegistry(PacketConfigurationOutRegistry.RegistryType.DAMAGE_TYPE));
		connection.sendPacket(new PacketConfigurationOutRegistry(PacketConfigurationOutRegistry.RegistryType.BANNER_PATTERN));
		connection.sendPacket(new PacketConfigurationOutRegistry(PacketConfigurationOutRegistry.RegistryType.ENCHANTMENT));
		connection.sendPacket(new PacketConfigurationOutRegistry(PacketConfigurationOutRegistry.RegistryType.JUKEBOX_SONG));

		// Update tags (packet logged)
		try (InputStream stream = HandlerLoginAcknowledge.class.getResourceAsStream(
			"/update_tags_767.bin")) {

			connection.getChannel().writeAndFlush(stream.readAllBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Finish
		connection.sendPacket(new PacketConfigurationOutFinish());
	}
}
