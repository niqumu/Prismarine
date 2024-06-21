package app.prismarine.server.net.handler.play;

import app.prismarine.server.entity.PrismarinePlayer;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.play.in.PacketPlayInPosition;
import org.bukkit.Location;

public class HandlerPlayPosition implements PacketHandler<PacketPlayInPosition> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketPlayInPosition packet) {
		if (connection.getPlayer() == null) {
			return;
		}

		PrismarinePlayer player = (PrismarinePlayer) connection.getPlayer();
		Location oldLocation = player.getLocation();
		Location newLocation = new Location(player.getWorld(), packet.getX(), packet.getY(), packet.getZ(),
			oldLocation.getYaw(), oldLocation.getPitch());

		player.setLocation(newLocation);
		player.setMoved(true);
	}
}
