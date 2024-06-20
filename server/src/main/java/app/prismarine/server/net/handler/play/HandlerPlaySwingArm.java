package app.prismarine.server.net.handler.play;

import app.prismarine.server.entity.PrismarinePlayer;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.play.in.PacketPlayInSwingArm;
import app.prismarine.server.net.packet.play.out.PacketPlayOutEntityAnimation;
import org.bukkit.entity.Player;

public class HandlerPlaySwingArm implements PacketHandler<PacketPlayInSwingArm> {

    /**
     * @param connection The connection this packet was received from
     * @param packet     The packet received by the server to handle
     */
    @Override
    public void handle(Connection connection, PacketPlayInSwingArm packet) {
        if (connection.getPlayer() == null) {
            return;
        }

        PrismarinePlayer sender = (PrismarinePlayer) connection.getPlayer();

        // Iterate over players to send them the animation
        sender.getWorld().getPlayers().forEach(rawPlayer -> {
            PrismarinePlayer player = (PrismarinePlayer) rawPlayer;

            // Only send the animation if the player can see the sender
            if (player.canSee(sender)) {
                player.getConnection().sendPacket(new PacketPlayOutEntityAnimation(sender,
                    PacketPlayOutEntityAnimation.Action.SWING_MAIN));
            }
        });
    }
}
