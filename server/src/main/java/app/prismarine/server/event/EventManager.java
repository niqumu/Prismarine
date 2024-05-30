package app.prismarine.server.event;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.*;

import java.util.HashSet;

/**
 * Class for the creation, management, and calling of events
 */
public class EventManager {

	/**
	 * Fires the given event, passing it to all listeners (plugins)
	 * @param event The event to fire
	 */
	private void fireEvent(Event event) {
		// todo plugin manager stuff
	}

	/**
	 * Handles a player chat event, creating a Bukkit event and dispatching it
	 * @param player The player who sent the message
	 * @param message The message sent by the player
	 * @return A new {@link AsyncPlayerChatEvent} representing the final event
	 */
	public AsyncPlayerChatEvent onPlayerChat(Player player, String message) {
		AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(false, player, message,
			new HashSet<>(Bukkit.getServer().getOnlinePlayers()));

		this.fireEvent(event);
		return event;
	}

	/**
	 * Handles a player join event, creating a Bukkit event and dispatching it
	 * @param player The player who is joining the server
	 * @return A new {@link PlayerJoinEvent} representing the final event
	 */
	public PlayerJoinEvent onPlayerJoin(Player player) {
		PlayerJoinEvent event = new PlayerJoinEvent(player,
			ChatColor.YELLOW + player.getName() + " joined the game");

		this.fireEvent(event);
		return event;
	}

	/**
	 * Handles a player quit event, creating a Bukkit event and dispatching it
	 * @param player The player who is disconnecting from the server
	 * @return A new {@link PlayerQuitEvent} representing the final event
	 */
	public PlayerQuitEvent onPlayerQuit(Player player) {
		PlayerQuitEvent event = new PlayerQuitEvent(player,
			ChatColor.YELLOW + player.getName() + " left the game");

		this.fireEvent(event);
		return event;
	}

	/**
	 * Handles a player kick event, creating a Bukkit event and dispatching it
	 * @param player The player who is being kicked from the server
	 * @param reason The reason provided for the kick
	 * @return A new {@link PlayerKickEvent} representing the final event
	 */
	public PlayerKickEvent onPlayerKick(Player player, String reason) {
		PlayerKickEvent event = new PlayerKickEvent(player, reason,
			ChatColor.YELLOW + player.getName() + " left the game");

		this.fireEvent(event);
		return event;
	}

}
