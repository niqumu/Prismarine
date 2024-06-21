package app.prismarine.server.net.handler.play;

import app.prismarine.server.entity.PrismarinePlayer;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.play.in.PacketPlayInRotation;
import org.bukkit.Location;

public class HandlerPlayRotation implements PacketHandler<PacketPlayInRotation> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketPlayInRotation packet) {
		if (connection.getPlayer() == null) {
			return;
		}

		PrismarinePlayer player = (PrismarinePlayer) connection.getPlayer();
		Location oldLocation = player.getLocation();
		Location newLocation = new Location(player.getWorld(), oldLocation.getX(), oldLocation.getY(), oldLocation.getZ(),
			packet.getYaw(), packet.getPitch());

		player.setLocation(newLocation);
		player.setRotated(true);
	}
}
