package app.prismarine.server.command.vanilla;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TpCommand extends Command {

	public TpCommand() {
		super("tp");
		this.setPermission("minecraft.command.tp");
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

		switch (args.length) {

			// Teleporting to a player
			case 1 -> {

			}

			default -> sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
		}

		return false;
	}
}
