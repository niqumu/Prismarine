package app.prismarine.server.net.handler.login;

import app.prismarine.server.net.Connection;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.configuration.PacketConfigurationOutFeatures;
import app.prismarine.server.net.packet.configuration.PacketConfigurationOutFinish;
import app.prismarine.server.net.packet.configuration.PacketConfigurationOutPacks;
import app.prismarine.server.net.packet.configuration.PacketConfigurationOutRegistry;
import app.prismarine.server.net.packet.login.PacketLoginInAcknowledge;

import java.io.InputStream;

import static app.prismarine.server.net.packet.configuration.PacketConfigurationOutRegistry.*;

public class HandlerLoginAcknowledge implements PacketHandler<PacketLoginInAcknowledge> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketLoginInAcknowledge packet) {
		connection.setState(ConnectionState.CONFIGURATION);
//		System.out.println("Switching state to CONFIGURATION");

		// Wait 500 ms for configuration to finish
		new Thread(() -> {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			this.finishConfiguration(connection);
		}).start();
	}

	private void finishConfiguration(Connection connection) {

		// Registry data
		connection.sendPacket(new PacketConfigurationOutRegistry(RegistryType.BIOME));
		connection.sendPacket(new PacketConfigurationOutRegistry(RegistryType.CHAT_TYPE));
		connection.sendPacket(new PacketConfigurationOutRegistry(RegistryType.TRIM_PATTERN));
		connection.sendPacket(new PacketConfigurationOutRegistry(RegistryType.TRIM_MATERIAL));
		connection.sendPacket(new PacketConfigurationOutRegistry(RegistryType.WOLF_VARIANT));
		connection.sendPacket(new PacketConfigurationOutRegistry(RegistryType.DIMENSION_TYPE));
		connection.sendPacket(new PacketConfigurationOutRegistry(RegistryType.DAMAGE_TYPE));
		connection.sendPacket(new PacketConfigurationOutRegistry(RegistryType.BANNER_PATTERN));

		// Update tags (packet logged)
//		try (InputStream stream = HandlerLoginAcknowledge.class.getResourceAsStream(
//			"/update-tags-packet.bin")) {
//
//			connection.getChannel().writeAndFlush(stream.readAllBytes());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		// Features
		connection.sendPacket(new PacketConfigurationOutFeatures("minecraft:vanilla"));

		// Resource packs, wait for a response before continuing
		connection.sendPacket(new PacketConfigurationOutPacks("minecraft", "core", "1.20.6"));
	}
}
