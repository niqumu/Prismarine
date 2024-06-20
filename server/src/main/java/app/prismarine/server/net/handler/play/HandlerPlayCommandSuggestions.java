package app.prismarine.server.net.handler.play;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.play.in.PacketPlayInCommandSuggestions;
import app.prismarine.server.net.packet.play.out.PacketPlayOutCommandSuggestions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class HandlerPlayCommandSuggestions implements PacketHandler<PacketPlayInCommandSuggestions> {

    /**
     * @param connection The connection this packet was received from
     * @param packet     The packet received by the server to handle
     */
    @Override
    public void handle(Connection connection, PacketPlayInCommandSuggestions packet) {
        Player sender = connection.getPlayer();
        List<String> completions = ((PrismarineServer) Bukkit.getServer())
            .getCommandMap().tabComplete(sender, packet.getText());

        if (completions != null) {
            connection.sendPacket(new PacketPlayOutCommandSuggestions(packet.getTransactionID(), 0, 0,
                completions.stream().map(PacketPlayOutCommandSuggestions.Match::new).toList()));
        }
    }
}
