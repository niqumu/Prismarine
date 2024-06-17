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

		// Features
		connection.sendPacket(new PacketConfigurationOutFeatures("minecraft:vanilla"));

		// Data packs
		connection.sendPacket(new PacketConfigurationOutPacks("minecraft", "core", "1.21"));
	}
}
