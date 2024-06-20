package app.prismarine.server.command.vanilla;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Stops a server. Saves all changes to disk, then shuts down the server.
 * Additionally, all players are kicked out of the server.
 *
 * @see <a href="https://minecraft.wiki/w/Commands/stop">https://minecraft.wiki/w/Commands/stop</a>
 * @author chloe
 */
public class StopCommand extends Command {

    public StopCommand() {
        super("stop");
        this.setPermission("minecraft.command.stop");
    }

    /**
     * Executes the command, returning its success
     *
     * @param sender       Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args         All arguments passed to the command, split via ' '
     * @return true if the command was successful, otherwise false
     */
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        Bukkit.getServer().shutdown();
        return true;
    }
}
