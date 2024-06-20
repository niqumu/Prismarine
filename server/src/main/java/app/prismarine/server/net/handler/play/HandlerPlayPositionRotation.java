package app.prismarine.server.net.handler.play;

import app.prismarine.server.entity.PrismarinePlayer;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.play.in.PacketPlayInPositionRotation;
import org.bukkit.Location;

public class HandlerPlayPositionRotation implements PacketHandler<PacketPlayInPositionRotation> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketPlayInPositionRotation packet) {
		if (connection.getPlayer() == null) {
			return;
		}

		PrismarinePlayer player = (PrismarinePlayer) connection.getPlayer();
		Location newLocation = new Location(player.getWorld(), packet.getX(), packet.getY(), packet.getZ(),
			packet.getYaw(), packet.getPitch());

		player.setLocation(newLocation);
	}
}
