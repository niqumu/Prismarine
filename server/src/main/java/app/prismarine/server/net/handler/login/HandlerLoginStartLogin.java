package app.prismarine.server.net.handler.login;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.login.PacketLoginInLoginStart;
import app.prismarine.server.net.packet.login.PacketLoginOutSuccess;
import app.prismarine.server.player.PrismarineOfflinePlayer;
import app.prismarine.server.player.PrismarinePlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class HandlerLoginStartLogin implements PacketHandler<PacketLoginInLoginStart> {
	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketLoginInLoginStart packet) {

		PrismarineServer.LOGGER.info("{} ({}) is logging into the server",
			packet.getName(), connection.getAddress());

		// The player that's trying to connect
		OfflinePlayer offlinePlayer = new PrismarineOfflinePlayer(
			new PrismarinePlayerProfile(packet.getName(), packet.getUuid()));

		// Ensure the player is whitelisted if whitelists are enabled
		if (Bukkit.getServer().hasWhitelist()) {

			// Kick the player if they're not whitelisted
			if (!offlinePlayer.isWhitelisted()) {
				connection.disconnect("You are not whitelisted!");
				return;
			}
		}

		// Associate the player with the connection
		connection.login(offlinePlayer.getPlayerProfile());

		// Inform the client that the login succeeded, and to continue the process
		connection.sendPacket(new PacketLoginOutSuccess(packet.getUuid(), packet.getName()));
	}
}
