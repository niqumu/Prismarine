package app.prismarine.server.entity;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.lists.PlayerWhitelist;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.play.out.PacketPlayOutSystemMessage;
import app.prismarine.server.player.PlayerConfiguration;
import app.prismarine.server.player.PrismarineOfflinePlayer;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.ban.IpBanList;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerExpCooldownChangeEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.ChannelNotRegisteredException;
import org.bukkit.plugin.messaging.MessageTooLargeException;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PrismarinePlayer extends PrismarineHumanEntity implements Player {

	/**
	 * The {@link Connection} this player is connecting through
	 */
	private final Connection connection;

	/**
	 * This player's {@link PlayerConfiguration}, representing client settings
	 */
	@Getter
	private final PlayerConfiguration configuration = new PlayerConfiguration();

	private String displayName;

	public PrismarinePlayer(@NotNull Server server, @NotNull Location location,
	                        @NotNull PlayerProfile profile, @NotNull Connection connection) {
		super(server, location, profile);
		this.connection = connection;
	}

	@Override
	public void tick() {
		this.connection.tick();
		super.tick();
	}

	/**
	 * Checks if this player is currently online
	 *
	 * @return true if they are online
	 */
	@Override
	public boolean isOnline() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a copy of the player's profile.
	 * <p>
	 * If the player is online, the returned profile will be complete.
	 * Otherwise, only the unique id is guaranteed to be present. You can use
	 * {@link PlayerProfile#update()} to complete the returned profile.
	 *
	 * @return the player's profile
	 */
	@Override
	public @NotNull PlayerProfile getPlayerProfile() {
		return this.profile;
	}

	/**
	 * Checks if this player has had their profile banned.
	 *
	 * @return true if banned, otherwise false
	 */
	@Override
	public boolean isBanned() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
	 * update the entry.
	 *
	 * @param reason  reason for the ban, null indicates implementation default
	 * @param expires date for the ban's expiration (unban), or null to imply
	 *                forever
	 * @param source  source of the ban, null indicates implementation default
	 * @return the entry for the newly created ban, or the entry for the
	 * (updated) previous ban
	 */
	@Override
	public @Nullable BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
	 * update the entry.
	 *
	 * @param reason  reason for the ban, null indicates implementation default
	 * @param expires instant for the ban's expiration (unban), or null to imply
	 *                forever
	 * @param source  source of the ban, null indicates implementation default
	 * @return the entry for the newly created ban, or the entry for the
	 * (updated) previous ban
	 */
	@Override
	public @Nullable BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Instant expires, @Nullable String source) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
	 * update the entry.
	 *
	 * @param reason   reason for the ban, null indicates implementation default
	 * @param duration how long the ban last, or null to imply
	 *                 forever
	 * @param source   source of the ban, null indicates implementation default
	 * @return the entry for the newly created ban, or the entry for the
	 * (updated) previous ban
	 */
	@Override
	public @Nullable BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Duration duration, @Nullable String source) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks if this player is whitelisted or not
	 *
	 * @return true if whitelisted
	 */
	@Override
	public boolean isWhitelisted() {
		for (OfflinePlayer player : ((PrismarineServer) Bukkit.getServer()).getWhitelist().getPlayers()) {
			if (player.getUniqueId().equals(this.getUniqueId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Sets if this player is whitelisted or not
	 *
	 * @param value true if whitelisted
	 */
	@Override
	public void setWhitelisted(boolean value) {
		if (this.isWhitelisted() == value) {
			return;
		}

		PlayerWhitelist whitelist = ((PrismarineServer) Bukkit.getServer()).getWhitelist();

		// If we're enabling the whitelist
		if (value) {
			whitelist.add(new PrismarineOfflinePlayer(this.getPlayerProfile()));
		}

		// Else, if we're disabling the whitelist
		else {
			whitelist.getPlayers().removeIf(offlinePlayer ->
				offlinePlayer.getUniqueId().equals(this.getUniqueId()));
		}
	}

	/**
	 * Gets a {@link Player} object that this represents, if there is one
	 * <p>
	 * If the player is online, this will return that player. Otherwise,
	 * it will return null.
	 *
	 * @return Online player
	 */
	@Override
	public @Nullable Player getPlayer() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the first date and time that this player was witnessed on this
	 * server.
	 * <p>
	 * If the player has never played before, this will return 0. Otherwise,
	 * it will be the amount of milliseconds since midnight, January 1, 1970
	 * UTC.
	 *
	 * @return Date of first log-in for this player, or 0
	 */
	@Override
	public long getFirstPlayed() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the last date and time that this player was witnessed on this
	 * server.
	 * <p>
	 * If the player has never played before, this will return 0. Otherwise,
	 * it will be the amount of milliseconds since midnight, January 1, 1970
	 * UTC.
	 *
	 * @return Date of last log-in for this player, or 0
	 */
	@Override
	public long getLastPlayed() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks if this player has played on this server before.
	 *
	 * @return True if the player has played before, otherwise false
	 */
	@Override
	public boolean hasPlayedBefore() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Increments the given statistic for this player.
	 * <p>
	 * This is equivalent to the following code:
	 * <code>incrementStatistic(Statistic, 1)</code>
	 *
	 * @param statistic Statistic to increment
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if the statistic requires an
	 *                                  additional parameter
	 */
	@Override
	public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Decrements the given statistic for this player.
	 * <p>
	 * This is equivalent to the following code:
	 * <code>decrementStatistic(Statistic, 1)</code>
	 *
	 * @param statistic Statistic to decrement
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if the statistic requires an
	 *                                  additional parameter
	 */
	@Override
	public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Increments the given statistic for this player.
	 *
	 * @param statistic Statistic to increment
	 * @param amount    Amount to increment this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the statistic requires an
	 *                                  additional parameter
	 */
	@Override
	public void incrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Decrements the given statistic for this player.
	 *
	 * @param statistic Statistic to decrement
	 * @param amount    Amount to decrement this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the statistic requires an
	 *                                  additional parameter
	 */
	@Override
	public void decrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the given statistic for this player.
	 *
	 * @param statistic Statistic to set
	 * @param newValue  The value to set this statistic to
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if newValue is negative
	 * @throws IllegalArgumentException if the statistic requires an
	 *                                  additional parameter
	 */
	@Override
	public void setStatistic(@NotNull Statistic statistic, int newValue) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the value of the given statistic for this player.
	 *
	 * @param statistic Statistic to check
	 * @return the value of the given statistic
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if the statistic requires an
	 *                                  additional parameter
	 */
	@Override
	public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Increments the given statistic for this player for the given material.
	 * <p>
	 * This is equivalent to the following code:
	 * <code>incrementStatistic(Statistic, Material, 1)</code>
	 *
	 * @param statistic Statistic to increment
	 * @param material  Material to offset the statistic with
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public void incrementStatistic(@NotNull Statistic statistic,
	                               @NotNull Material material) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Decrements the given statistic for this player for the given material.
	 * <p>
	 * This is equivalent to the following code:
	 * <code>decrementStatistic(Statistic, Material, 1)</code>
	 *
	 * @param statistic Statistic to decrement
	 * @param material  Material to offset the statistic with
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public void decrementStatistic(@NotNull Statistic statistic,
	                               @NotNull Material material) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the value of the given statistic for this player.
	 *
	 * @param statistic Statistic to check
	 * @param material  Material offset of the statistic
	 * @return the value of the given statistic
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public int getStatistic(@NotNull Statistic statistic,
	                        @NotNull Material material) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Increments the given statistic for this player for the given material.
	 *
	 * @param statistic Statistic to increment
	 * @param material  Material to offset the statistic with
	 * @param amount    Amount to increment this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material,
	                               int amount) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Decrements the given statistic for this player for the given material.
	 *
	 * @param statistic Statistic to decrement
	 * @param material  Material to offset the statistic with
	 * @param amount    Amount to decrement this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material,
	                               int amount) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the given statistic for this player for the given material.
	 *
	 * @param statistic Statistic to set
	 * @param material  Material to offset the statistic with
	 * @param newValue  The value to set this statistic to
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if newValue is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public void setStatistic(@NotNull Statistic statistic, @NotNull Material material,
	                         int newValue) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Increments the given statistic for this player for the given entity.
	 * <p>
	 * This is equivalent to the following code:
	 * <code>incrementStatistic(Statistic, EntityType, 1)</code>
	 *
	 * @param statistic  Statistic to increment
	 * @param entityType EntityType to offset the statistic with
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public void incrementStatistic(@NotNull Statistic statistic,
	                               @NotNull EntityType entityType) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Decrements the given statistic for this player for the given entity.
	 * <p>
	 * This is equivalent to the following code:
	 * <code>decrementStatistic(Statistic, EntityType, 1)</code>
	 *
	 * @param statistic  Statistic to decrement
	 * @param entityType EntityType to offset the statistic with
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public void decrementStatistic(@NotNull Statistic statistic,
	                               @NotNull EntityType entityType) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the value of the given statistic for this player.
	 *
	 * @param statistic  Statistic to check
	 * @param entityType EntityType offset of the statistic
	 * @return the value of the given statistic
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public int getStatistic(@NotNull Statistic statistic,
	                        @NotNull EntityType entityType) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Increments the given statistic for this player for the given entity.
	 *
	 * @param statistic  Statistic to increment
	 * @param entityType EntityType to offset the statistic with
	 * @param amount     Amount to increment this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType,
	                               int amount) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Decrements the given statistic for this player for the given entity.
	 *
	 * @param statistic  Statistic to decrement
	 * @param entityType EntityType to offset the statistic with
	 * @param amount     Amount to decrement this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the given statistic for this player for the given entity.
	 *
	 * @param statistic  Statistic to set
	 * @param entityType EntityType to offset the statistic with
	 * @param newValue   The value to set this statistic to
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if newValue is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *                                  for the statistic
	 */
	@Override
	public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int newValue) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates a Map representation of this class.
	 * <p>
	 * This class must provide a method to restore this class, as defined in
	 * the {@link ConfigurationSerializable} interface javadocs.
	 *
	 * @return Map containing the current state of this class
	 */
	@Override
	public @NotNull Map<String, Object> serialize() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Tests to see of a Conversable object is actively engaged in a
	 * conversation.
	 *
	 * @return True if a conversation is in progress
	 */
	@Override
	public boolean isConversing() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Accepts input into the active conversation. If no conversation is in
	 * progress, this method does nothing.
	 *
	 * @param input The input message into the conversation
	 */
	@Override
	public void acceptConversationInput(@NotNull String input) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Enters into a dialog with a Conversation object.
	 *
	 * @param conversation The conversation to begin
	 * @return True if the conversation should proceed, false if it has been
	 * enqueued
	 */
	@Override
	public boolean beginConversation(@NotNull Conversation conversation) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Abandons an active conversation.
	 *
	 * @param conversation The conversation to abandon
	 */
	@Override
	public void abandonConversation(@NotNull Conversation conversation) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Abandons an active conversation.
	 *
	 * @param conversation The conversation to abandon
	 * @param details      Details about why the conversation was abandoned
	 */
	@Override
	public void abandonConversation(@NotNull Conversation conversation,
	                                @NotNull ConversationAbandonedEvent details) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sends this sender a message
	 *
	 * @param message Message to be displayed
	 */
	@Override
	public void sendMessage(@NotNull String message) {
		this.connection.sendPacket(new PacketPlayOutSystemMessage(message, false));
	}

	/**
	 * Sends this sender multiple messages
	 *
	 * @param messages An array of messages to be displayed
	 */
	@Override
	public void sendMessage(@NotNull String... messages) {
		for (String message : messages) {
			this.sendMessage(message);
		}
	}

	/**
	 * Sends this sender a message
	 *
	 * @param message Message to be displayed
	 * @param sender The sender of this message
	 */
	@Override
	public void sendMessage(@Nullable UUID sender, @NotNull String message) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sends this sender multiple messages
	 *
	 * @param messages An array of messages to be displayed
	 * @param sender The sender of this message
	 */
	@Override
	public void sendMessage(@Nullable UUID sender, @NotNull String... messages) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sends this sender a message raw
	 *
	 * @param sender  The sender of this message
	 * @param message Message to be displayed
	 */
	@Override
	public void sendRawMessage(@Nullable UUID sender, @NotNull String message) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the "friendly" name to display of this player. This may include
	 * color.
	 * <p>
	 * Note that this name will not be displayed in game, only in chat and
	 * places defined by plugins.
	 *
	 * @return the friendly name
	 */
	@Override
	public @NotNull String getDisplayName() {
		return this.displayName == null ? this.getName() : this.displayName;
	}

	/**
	 * Sets the "friendly" name to display of this player. This may include
	 * color.
	 * <p>
	 * Note that this name will not be displayed in game, only in chat and
	 * places defined by plugins.
	 *
	 * @param name The new display name.
	 */
	@Override
	public void setDisplayName(@Nullable String name) {
		this.displayName = name;
	}

	/**
	 * Gets the name that is shown on the player list.
	 *
	 * @return the player list name
	 */
	@Override
	public @NotNull String getPlayerListName() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the name that is shown on the in-game player list.
	 * <p>
	 * If the value is null, the name will be identical to {@link #getName()}.
	 *
	 * @param name new player list name
	 */
	@Override
	public void setPlayerListName(@Nullable String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the currently displayed player list header for this player.
	 *
	 * @return player list header or null
	 */
	@Override
	public @Nullable String getPlayerListHeader() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the currently displayed player list footer for this player.
	 *
	 * @return player list header or null
	 */
	@Override
	public @Nullable String getPlayerListFooter() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the currently displayed player list header for this player.
	 *
	 * @param header player list header, null for empty
	 */
	@Override
	public void setPlayerListHeader(@Nullable String header) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the currently displayed player list footer for this player.
	 *
	 * @param footer player list footer, null for empty
	 */
	@Override
	public void setPlayerListFooter(@Nullable String footer) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the currently displayed player list header and footer for this
	 * player.
	 *
	 * @param header player list header, null for empty
	 * @param footer player list footer, null for empty
	 */
	@Override
	public void setPlayerListHeaderFooter(@Nullable String header, @Nullable String footer) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Set the target of the player's compass.
	 *
	 * @param loc Location to point to
	 */
	@Override
	public void setCompassTarget(@NotNull Location loc) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the previously set compass target.
	 *
	 * @return location of the target
	 */
	@Override
	public @NotNull Location getCompassTarget() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the socket address of this player
	 *
	 * @return the player's address
	 */
	@Override
	public @Nullable InetSocketAddress getAddress() {
		return this.connection.getAddress();
	}

	/**
	 * Gets if this connection has been transferred from another server.
	 *
	 * @return true if the connection has been transferred
	 */
	@Override
	public boolean isTransferred() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Retrieves a cookie from this player.
	 *
	 * @param key the key identifying the cookie cookie
	 * @return a {@link CompletableFuture} that will be completed when the
	 * Cookie response is received or otherwise available. If the cookie is not
	 * set in the client, the {@link CompletableFuture} will complete with a
	 * null value.
	 */
	@Override
	public @NotNull CompletableFuture<byte[]> retrieveCookie(@NotNull NamespacedKey key) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Stores a cookie in this player's client.
	 *
	 * @param key   the key identifying the cookie cookie
	 * @param value the data to store in the cookie
	 * @throws IllegalStateException if a cookie cannot be stored at this time
	 */
	@Override
	public void storeCookie(@NotNull NamespacedKey key, byte @NotNull [] value) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Requests this player to connect to a different server specified by host
	 * and port.
	 *
	 * @param host the host of the server to transfer to
	 * @param port the port of the server to transfer to
	 * @throws IllegalStateException if a transfer cannot take place at this
	 *                               time
	 */
	@Override
	public void transfer(@NotNull String host, int port) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sends this sender a message raw
	 *
	 * @param message Message to be displayed
	 */
	@Override
	public void sendRawMessage(@NotNull String message) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Kicks player with custom kick message.
	 *
	 * @param message kick message
	 */
	@Override
	public void kickPlayer(@Nullable String message) {
		this.connection.disconnect(message);
	}

	/**
	 * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
	 * update the entry.
	 *
	 * @param reason     reason for the ban, null indicates implementation default
	 * @param expires    date for the ban's expiration (unban), or null to imply
	 *                   forever
	 * @param source     source of the ban, null indicates implementation default
	 * @param kickPlayer if the player need to be kick
	 * @return the entry for the newly created ban, or the entry for the
	 * (updated) previous ban
	 */
	@Override
	public @Nullable BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Date expires,
	                                             @Nullable String source, boolean kickPlayer) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
	 * update the entry.
	 *
	 * @param reason     reason for the ban, null indicates implementation default
	 * @param expires    date for the ban's expiration (unban), or null to imply
	 *                   forever
	 * @param source     source of the ban, null indicates implementation default
	 * @param kickPlayer if the player need to be kick
	 * @return the entry for the newly created ban, or the entry for the
	 * (updated) previous ban
	 */
	@Override
	public @Nullable BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Instant expires,
	                                             @Nullable String source, boolean kickPlayer) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
	 * update the entry.
	 *
	 * @param reason     reason for the ban, null indicates implementation default
	 * @param duration   the duration how long the ban lasts, or null to imply
	 *                   forever
	 * @param source     source of the ban, null indicates implementation default
	 * @param kickPlayer if the player need to be kick
	 * @return the entry for the newly created ban, or the entry for the
	 * (updated) previous ban
	 */
	@Override
	public @Nullable BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Duration duration,
	                                             @Nullable String source, boolean kickPlayer) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds this user's current IP address to the {@link IpBanList}. If a previous ban exists, this will
	 * update the entry. If {@link #getAddress()} is null this method will throw an exception.
	 *
	 * @param reason     reason for the ban, null indicates implementation default
	 * @param expires    date for the ban's expiration (unban), or null to imply
	 *                   forever
	 * @param source     source of the ban, null indicates implementation default
	 * @param kickPlayer if the player need to be kick
	 * @return the entry for the newly created ban, or the entry for the
	 * (updated) previous ban
	 */
	@Override
	public @Nullable BanEntry<InetAddress> banIp(@Nullable String reason, @Nullable Date expires,
	                                             @Nullable String source, boolean kickPlayer) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds this user's current IP address to the {@link IpBanList}. If a previous ban exists, this will
	 * update the entry. If {@link #getAddress()} is null this method will throw an exception.
	 *
	 * @param reason     reason for the ban, null indicates implementation default
	 * @param expires    date for the ban's expiration (unban), or null to imply
	 *                   forever
	 * @param source     source of the ban, null indicates implementation default
	 * @param kickPlayer if the player need to be kick
	 * @return the entry for the newly created ban, or the entry for the
	 * (updated) previous ban
	 */
	@Override
	public @Nullable BanEntry<InetAddress> banIp(@Nullable String reason, @Nullable Instant expires,
	                                             @Nullable String source, boolean kickPlayer) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds this user's current IP address to the {@link IpBanList}. If a previous ban exists, this will
	 * update the entry. If {@link #getAddress()} is null this method will throw an exception.
	 *
	 * @param reason     reason for the ban, null indicates implementation default
	 * @param duration   the duration how long the ban lasts, or null to imply
	 *                   forever
	 * @param source     source of the ban, null indicates implementation default
	 * @param kickPlayer if the player need to be kick
	 * @return the entry for the newly created ban, or the entry for the
	 * (updated) previous ban
	 */
	@Override
	public @Nullable BanEntry<InetAddress> banIp(@Nullable String reason, @Nullable Duration duration,
	                                             @Nullable String source, boolean kickPlayer) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Says a message (or runs a command).
	 *
	 * @param msg message to print
	 */
	@Override
	public void chat(@NotNull String msg) {

		AsyncPlayerChatEvent event = ((PrismarineServer) Bukkit.getServer()).
			getEventManager().onPlayerChat(this, msg);

		if (!event.isCancelled()) {
			String message = String.format(event.getFormat(), this.getDisplayName(), event.getMessage());

			event.getRecipients().forEach(player -> player.sendMessage(message));
			PrismarineServer.LOGGER.info(message);
		}
	}

	/**
	 * Makes the player perform the given command
	 *
	 * @param command Command to perform
	 * @return true if the command was successful, otherwise false
	 */
	@Override
	public boolean performCommand(@NotNull String command) {
		return Bukkit.getServer().dispatchCommand(this, command);
	}

	/**
	 * Returns if the player is in sneak mode
	 *
	 * @return true if player is in sneak mode
	 */
	@Override
	public boolean isSneaking() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the sneak mode the player
	 *
	 * @param sneak true if player should appear sneaking
	 */
	@Override
	public void setSneaking(boolean sneak) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the player is sprinting or not.
	 *
	 * @return true if player is sprinting.
	 */
	@Override
	public boolean isSprinting() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether the player is sprinting or not.
	 *
	 * @param sprinting true if the player should be sprinting
	 */
	@Override
	public void setSprinting(boolean sprinting) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Saves the players current location, health, inventory, motion, and
	 * other information into the username.dat file, in the world/player
	 * folder
	 */
	@Override
	public void saveData() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Loads the players current location, health, inventory, motion, and
	 * other information from the username.dat file, in the world/player
	 * folder.
	 * <p>
	 * Note: This will overwrite the players current inventory, health,
	 * motion, etc, with the state from the saved dat file.
	 */
	@Override
	public void loadData() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether the player is ignored as not sleeping. If everyone is
	 * either sleeping or has this flag set, then time will advance to the
	 * next day. If everyone has this flag set but no one is actually in bed,
	 * then nothing will happen.
	 *
	 * @param isSleeping Whether to ignore.
	 */
	@Override
	public void setSleepingIgnored(boolean isSleeping) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns whether the player is sleeping ignored.
	 *
	 * @return Whether player is ignoring sleep.
	 */
	@Override
	public boolean isSleepingIgnored() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the Location where the player will spawn at their bed, null if
	 * they have not slept in one or their current bed spawn is invalid.
	 *
	 * @return Bed Spawn Location if bed exists, otherwise null.
	 * @see #getRespawnLocation()
	 * @deprecated Misleading name. This method also returns the location of
	 * respawn anchors.
	 */
	@Override
	public @Nullable Location getBedSpawnLocation() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the Location where the player will spawn at, null if they
	 * don't have a valid respawn point.
	 *
	 * @return respawn location if exists, otherwise null.
	 */
	@Override
	public @Nullable Location getRespawnLocation() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the Location where the player will spawn at their bed.
	 *
	 * @param location where to set the respawn location
	 * @see #setRespawnLocation(Location)
	 * @deprecated Misleading name. This method sets the player's respawn
	 * location more generally and is not limited to beds.
	 */
	@Override
	public void setBedSpawnLocation(@Nullable Location location) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the Location where the player will respawn.
	 *
	 * @param location where to set the respawn location
	 */
	@Override
	public void setRespawnLocation(@Nullable Location location) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the Location where the player will spawn at their bed.
	 *
	 * @param location where to set the respawn location
	 * @param force    whether to forcefully set the respawn location even if a
	 *                 valid bed is not present
	 * @see #setRespawnLocation(Location, boolean)
	 * @deprecated Misleading name. This method sets the player's respawn
	 * location more generally and is not limited to beds.
	 */
	@Override
	public void setBedSpawnLocation(@Nullable Location location, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the Location where the player will respawn.
	 *
	 * @param location where to set the respawn location
	 * @param force    whether to forcefully set the respawn location even if a
	 *                 valid respawn point is not present
	 */
	@Override
	public void setRespawnLocation(@Nullable Location location, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a note for the player at a location. <br>
	 * This <i>will</i> work with cake.
	 *
	 * @param loc        The location to play the note
	 * @param instrument The instrument ID.
	 * @param note       The note ID.
	 * @deprecated Magic value
	 */
	@Override
	public void playNote(@NotNull Location loc, byte instrument, byte note) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a note for the player at a location. <br>
	 * This <i>will</i> work with cake.
	 * <p>
	 * This method will fail silently when called with {@link Instrument#CUSTOM_HEAD}.
	 *
	 * @param loc        The location to play the note
	 * @param instrument The instrument
	 * @param note       The note
	 */
	@Override
	public void playNote(@NotNull Location loc, @NotNull Instrument instrument, @NotNull Note note) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location.
	 * <p>
	 * This function will fail silently if Location or Sound are null.
	 *
	 * @param location The location to play the sound
	 * @param sound    The sound to play
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 */
	@Override
	public void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location.
	 * <p>
	 * This function will fail silently if Location or Sound are null. No
	 * sound will be heard by the player if their client does not have the
	 * respective sound for the value passed.
	 *
	 * @param location The location to play the sound
	 * @param sound    The internal sound name to play
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 */
	@Override
	public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location.
	 * <p>
	 * This function will fail silently if Location or Sound are null.
	 *
	 * @param location The location to play the sound
	 * @param sound    The sound to play
	 * @param category The category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 */
	@Override
	public void playSound(@NotNull Location location, @NotNull Sound sound,
	                      @NotNull SoundCategory category, float volume, float pitch) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location.
	 * <p>
	 * This function will fail silently if Location or Sound are null. No sound
	 * will be heard by the player if their client does not have the respective
	 * sound for the value passed.
	 *
	 * @param location The location to play the sound
	 * @param sound    The internal sound name to play
	 * @param category The category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 */
	@Override
	public void playSound(@NotNull Location location, @NotNull String sound,
	                      @NotNull SoundCategory category, float volume, float pitch) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location. For sounds with multiple
	 * variations passing the same seed will always play the same variation.
	 * <p>
	 * This function will fail silently if Location or Sound are null.
	 *
	 * @param location The location to play the sound
	 * @param sound    The sound to play
	 * @param category The category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 * @param seed     The seed for the sound
	 */
	@Override
	public void playSound(@NotNull Location location, @NotNull Sound sound,
	                      @NotNull SoundCategory category, float volume, float pitch, long seed) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location. For sounds with multiple
	 * variations passing the same seed will always play the same variation.
	 * <p>
	 * This function will fail silently if Location or Sound are null. No sound
	 * will be heard by the player if their client does not have the respective
	 * sound for the value passed.
	 *
	 * @param location The location to play the sound
	 * @param sound    The internal sound name to play
	 * @param category The category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 * @param seed     The seed for the sound
	 */
	@Override
	public void playSound(@NotNull Location location, @NotNull String sound,
	                      @NotNull SoundCategory category, float volume, float pitch, long seed) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location of the entity.
	 * <p>
	 * This function will fail silently if Entity or Sound are null.
	 *
	 * @param entity The entity to play the sound
	 * @param sound  The sound to play
	 * @param volume The volume of the sound
	 * @param pitch  The pitch of the sound
	 */
	@Override
	public void playSound(@NotNull Entity entity, @NotNull Sound sound, float volume, float pitch) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location of the entity.
	 * <p>
	 * This function will fail silently if Entity or Sound are null.
	 *
	 * @param entity The entity to play the sound
	 * @param sound  The sound to play
	 * @param volume The volume of the sound
	 * @param pitch  The pitch of the sound
	 */
	@Override
	public void playSound(@NotNull Entity entity, @NotNull String sound, float volume, float pitch) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location of the entity.
	 * <p>
	 * This function will fail silently if Entity or Sound are null.
	 *
	 * @param entity   The entity to play the sound
	 * @param sound    The sound to play
	 * @param category The category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 */
	@Override
	public void playSound(@NotNull Entity entity, @NotNull Sound sound,
	                      @NotNull SoundCategory category, float volume, float pitch) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location of the entity.
	 * <p>
	 * This function will fail silently if Entity or Sound are null.
	 *
	 * @param entity   The entity to play the sound
	 * @param sound    The sound to play
	 * @param category The category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 */
	@Override
	public void playSound(@NotNull Entity entity, @NotNull String sound,
	                      @NotNull SoundCategory category, float volume, float pitch) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location of the entity. For sounds with
	 * multiple variations passing the same seed will always play the same variation.
	 * <p>
	 * This function will fail silently if Entity or Sound are null.
	 *
	 * @param entity   The entity to play the sound
	 * @param sound    The sound to play
	 * @param category The category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 * @param seed     The seed for the sound
	 */
	@Override
	public void playSound(@NotNull Entity entity, @NotNull Sound sound,
	                      @NotNull SoundCategory category, float volume, float pitch, long seed) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Play a sound for a player at the location of the entity. For sounds with
	 * multiple variations passing the same seed will always play the same variation.
	 * <p>
	 * This function will fail silently if Entity or Sound are null.
	 *
	 * @param entity   The entity to play the sound
	 * @param sound    The sound to play
	 * @param category The category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 * @param seed     The seed for the sound
	 */
	@Override
	public void playSound(@NotNull Entity entity, @NotNull String sound,
	                      @NotNull SoundCategory category, float volume, float pitch, long seed) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Stop the specified sound from playing.
	 *
	 * @param sound the sound to stop
	 */
	@Override
	public void stopSound(@NotNull Sound sound) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Stop the specified sound from playing.
	 *
	 * @param sound the sound to stop
	 */
	@Override
	public void stopSound(@NotNull String sound) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Stop the specified sound from playing.
	 *
	 * @param sound    the sound to stop
	 * @param category the category of the sound
	 */
	@Override
	public void stopSound(@NotNull Sound sound, @Nullable SoundCategory category) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Stop the specified sound from playing.
	 *
	 * @param sound    the sound to stop
	 * @param category the category of the sound
	 */
	@Override
	public void stopSound(@NotNull String sound, @Nullable SoundCategory category) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Stop the specified sound category from playing.
	 *
	 * @param category the sound category to stop
	 */
	@Override
	public void stopSound(@NotNull SoundCategory category) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Stop all sounds from playing.
	 */
	@Override
	public void stopAllSounds() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Plays an effect to just this player.
	 *
	 * @param loc    the location to play the effect at
	 * @param effect the {@link Effect}
	 * @param data   a data bit needed for some effects
	 * @deprecated Magic value
	 */
	@Override
	public void playEffect(@NotNull Location loc, @NotNull Effect effect, int data) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Plays an effect to just this player.
	 *
	 * @param loc    the location to play the effect at
	 * @param effect the {@link Effect}
	 * @param data   a data bit needed for some effects
	 */
	@Override
	public <T> void playEffect(@NotNull Location loc, @NotNull Effect effect, @Nullable T data) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Force this player to break a Block using the item in their main hand.
	 * <p>
	 * This method will respect enchantments, handle item durability (if
	 * applicable) and drop experience and the correct items according to the
	 * tool/item in the player's hand.
	 * <p>
	 * Note that this method will call a {@link BlockBreakEvent}, meaning that
	 * this method may not be successful in breaking the block if the event was
	 * cancelled by a third party plugin. Care should be taken if running this
	 * method in a BlockBreakEvent listener as recursion may be possible if it
	 * is invoked on the same {@link Block} being broken in the event.
	 * <p>
	 * Additionally, a {@link BlockDropItemEvent} is called for the items
	 * dropped by this method (if successful).
	 * <p>
	 * The block must be in the same world as the player.
	 *
	 * @param block the block to break
	 * @return true if the block was broken, false if the break failed
	 */
	@Override
	public boolean breakBlock(@NotNull Block block) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send a block change. This fakes a block change packet for a user at a
	 * certain location. This will not actually change the world in any way.
	 *
	 * @param loc      The location of the changed block
	 * @param material The new block
	 * @param data     The block data
	 * @deprecated Magic value
	 */
	@Override
	public void sendBlockChange(@NotNull Location loc, @NotNull Material material, byte data) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send a block change. This fakes a block change packet for a user at a
	 * certain location. This will not actually change the world in any way.
	 *
	 * @param loc   The location of the changed block
	 * @param block The new block
	 */
	@Override
	public void sendBlockChange(@NotNull Location loc, @NotNull BlockData block) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send a multi-block change. This fakes a block change packet for a user
	 * at multiple locations. This will not actually change the world in any
	 * way.
	 * <p>
	 * This method may send multiple packets to the client depending on the
	 * blocks in the collection. A packet must be sent for each chunk section
	 * modified, meaning one packet for each 16x16x16 block area. Even if only
	 * one block is changed in two different chunk sections, two packets will
	 * be sent.
	 * <p>
	 * Additionally, this method cannot guarantee the functionality of changes
	 * being sent to the player in chunks not loaded by the client. It is the
	 * responsibility of the caller to ensure that the client is within range
	 * of the changed blocks or to handle any side effects caused as a result.
	 *
	 * @param blocks the block states to send to the player
	 */
	@Override
	public void sendBlockChanges(@NotNull Collection<BlockState> blocks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send a multi-block change. This fakes a block change packet for a user
	 * at multiple locations. This will not actually change the world in any
	 * way.
	 * <p>
	 * This method may send multiple packets to the client depending on the
	 * blocks in the collection. A packet must be sent for each chunk section
	 * modified, meaning one packet for each 16x16x16 block area. Even if only
	 * one block is changed in two different chunk sections, two packets will
	 * be sent.
	 * <p>
	 * Additionally, this method cannot guarantee the functionality of changes
	 * being sent to the player in chunks not loaded by the client. It is the
	 * responsibility of the caller to ensure that the client is within range
	 * of the changed blocks or to handle any side effects caused as a result.
	 *
	 * @param blocks               the block states to send to the player
	 * @param suppressLightUpdates whether or not light updates should be
	 *                             suppressed when updating the blocks on the client
	 * @deprecated suppressLightUpdates is not functional in versions greater
	 * than 1.19.4
	 */
	@Override
	public void sendBlockChanges(@NotNull Collection<BlockState> blocks, boolean suppressLightUpdates) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send block damage. This fakes block break progress at a certain location
	 * sourced by this player. This will not actually change the block's break
	 * progress in any way.
	 *
	 * @param loc      the location of the damaged block
	 * @param progress the progress from 0.0 - 1.0 where 0 is no damage and
	 *                 1.0 is the most damaged
	 */
	@Override
	public void sendBlockDamage(@NotNull Location loc, float progress) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send block damage. This fakes block break progress at a certain location
	 * sourced by the provided entity. This will not actually change the block's
	 * break progress in any way.
	 * <p>
	 * At the same location for each unique damage source sent to the player, a
	 * separate damage overlay will be displayed with the given progress. This allows
	 * for block damage at different progress from multiple entities at once.
	 *
	 * @param loc      the location of the damaged block
	 * @param progress the progress from 0.0 - 1.0 where 0 is no damage and
	 *                 1.0 is the most damaged
	 * @param source   the entity to which the damage belongs
	 */
	@Override
	public void sendBlockDamage(@NotNull Location loc, float progress, @NotNull Entity source) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send block damage. This fakes block break progress at a certain location
	 * sourced by the provided entity id. This will not actually change the block's
	 * break progress in any way.
	 * <p>
	 * At the same location for each unique damage source sent to the player, a
	 * separate damage overlay will be displayed with the given progress. This allows
	 * for block damage at different progress from multiple entities at once.
	 *
	 * @param loc      the location of the damaged block
	 * @param progress the progress from 0.0 - 1.0 where 0 is no damage and
	 *                 1.0 is the most damaged
	 * @param sourceId the entity id of the entity to which the damage belongs.
	 *                 Can be an id that does not associate directly with an existing or loaded entity.
	 */
	@Override
	public void sendBlockDamage(@NotNull Location loc, float progress, int sourceId) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send an equipment change for the target entity. This will not
	 * actually change the entity's equipment in any way.
	 *
	 * @param entity the entity whose equipment to change
	 * @param slot   the slot to change
	 * @param item   the item to which the slot should be changed, or null to set
	 *               it to air
	 */
	@Override
	public void sendEquipmentChange(@NotNull LivingEntity entity, @NotNull EquipmentSlot slot, @Nullable ItemStack item) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send multiple equipment changes for the target entity. This will not
	 * actually change the entity's equipment in any way.
	 *
	 * @param entity the entity whose equipment to change
	 * @param items  the slots to change, where the values are the items to which
	 *               the slot should be changed. null values will set the slot to air
	 */
	@Override
	public void sendEquipmentChange(@NotNull LivingEntity entity, @NotNull Map<EquipmentSlot, ItemStack> items) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send a sign change. This fakes a sign change packet for a user at
	 * a certain location. This will not actually change the world in any way.
	 * This method will use a sign at the location's block or a faked sign
	 * sent via
	 * {@link #sendBlockChange(Location, BlockData)}.
	 * <p>
	 * If the client does not have a sign at the given location it will
	 * display an error message to the user.
	 * <p>
	 * To change all attributes of a sign, including the back Side, use
	 * {@link #sendBlockUpdate(Location, TileState)}.
	 *
	 * @param loc   the location of the sign
	 * @param lines the new text on the sign or null to clear it
	 * @throws IllegalArgumentException if location is null
	 * @throws IllegalArgumentException if lines is non-null and has a length less than 4
	 */
	@Override
	public void sendSignChange(@NotNull Location loc, @Nullable String[] lines) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send a sign change. This fakes a sign change packet for a user at
	 * a certain location. This will not actually change the world in any way.
	 * This method will use a sign at the location's block or a faked sign
	 * sent via
	 * {@link #sendBlockChange(Location, BlockData)}.
	 * <p>
	 * If the client does not have a sign at the given location it will
	 * display an error message to the user.
	 * <p>
	 * To change all attributes of a sign, including the back Side, use
	 * {@link #sendBlockUpdate(Location, TileState)}.
	 *
	 * @param loc      the location of the sign
	 * @param lines    the new text on the sign or null to clear it
	 * @param dyeColor the color of the sign
	 * @throws IllegalArgumentException if location is null
	 * @throws IllegalArgumentException if dyeColor is null
	 * @throws IllegalArgumentException if lines is non-null and has a length less than 4
	 */
	@Override
	public void sendSignChange(@NotNull Location loc, @Nullable String[] lines,
	                           @NotNull DyeColor dyeColor) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send a sign change. This fakes a sign change packet for a user at
	 * a certain location. This will not actually change the world in any way.
	 * This method will use a sign at the location's block or a faked sign
	 * sent via
	 * {@link #sendBlockChange(Location, BlockData)}.
	 * <p>
	 * If the client does not have a sign at the given location it will
	 * display an error message to the user.
	 * <p>
	 * To change all attributes of a sign, including the back Side, use
	 * {@link #sendBlockUpdate(Location, TileState)}.
	 *
	 * @param loc            the location of the sign
	 * @param lines          the new text on the sign or null to clear it
	 * @param dyeColor       the color of the sign
	 * @param hasGlowingText if the sign's text should be glowing
	 * @throws IllegalArgumentException if location is null
	 * @throws IllegalArgumentException if dyeColor is null
	 * @throws IllegalArgumentException if lines is non-null and has a length less than 4
	 */
	@Override
	public void sendSignChange(@NotNull Location loc, @Nullable String[] lines,
	                           @NotNull DyeColor dyeColor, boolean hasGlowingText) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send a TileState change. This fakes a TileState change for a user at
	 * the given location. This will not actually change the world in any way.
	 * This method will use a TileState at the location's block or a faked TileState
	 * sent via
	 * {@link #sendBlockChange(Location, BlockData)}.
	 * <p>
	 * If the client does not have an appropriate tile at the given location it
	 * may display an error message to the user.
	 * <p>
	 * {@link BlockData#createBlockState()} can be used to create a {@link BlockState}.
	 *
	 * @param loc       the location of the sign
	 * @param tileState the tile state
	 * @throws IllegalArgumentException if location is null
	 * @throws IllegalArgumentException if tileState is null
	 */
	@Override
	public void sendBlockUpdate(@NotNull Location loc, @NotNull TileState tileState) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Change a potion effect for the target entity. This will not actually
	 * change the entity's potion effects in any way.
	 * <p>
	 * <b>Note:</b> Sending an effect change to a player for themselves may
	 * cause unexpected behavior on the client. Effects sent this way will also
	 * not be removed when their timer reaches 0, they can be removed with
	 * {@link #sendPotionEffectChangeRemove(LivingEntity, PotionEffectType)}
	 *
	 * @param entity the entity whose potion effects to change
	 * @param effect the effect to change
	 */
	@Override
	public void sendPotionEffectChange(@NotNull LivingEntity entity, @NotNull PotionEffect effect) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Remove a potion effect for the target entity. This will not actually
	 * change the entity's potion effects in any way.
	 * <p>
	 * <b>Note:</b> Sending an effect change to a player for themselves may
	 * cause unexpected behavior on the client.
	 *
	 * @param entity the entity whose potion effects to change
	 * @param type   the effect type to remove
	 */
	@Override
	public void sendPotionEffectChangeRemove(@NotNull LivingEntity entity, @NotNull PotionEffectType type) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Render a map and send it to the player in its entirety. This may be
	 * used when streaming the map in the normal manner is not desirable.
	 *
	 * @param map The map to be sent
	 */
	@Override
	public void sendMap(@NotNull MapView map) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send a hurt animation. This fakes incoming damage towards the player from
	 * the given yaw relative to the player's direction.
	 *
	 * @param yaw the yaw in degrees relative to the player's direction where 0
	 *            is in front of the player, 90 is to the right, 180 is behind, and 270 is
	 *            to the left
	 */
	@Override
	public void sendHurtAnimation(float yaw) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Add custom chat completion suggestions shown to the player while typing a
	 * message.
	 *
	 * @param completions the completions to send
	 */
	@Override
	public void addCustomChatCompletions(@NotNull Collection<String> completions) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Remove custom chat completion suggestions shown to the player while
	 * typing a message.
	 * <p>
	 * Online player names cannot be removed with this method. This will affect
	 * only custom completions added by {@link #addCustomChatCompletions(Collection)}
	 * or {@link #setCustomChatCompletions(Collection)}.
	 *
	 * @param completions the completions to remove
	 */
	@Override
	public void removeCustomChatCompletions(@NotNull Collection<String> completions) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Set the list of chat completion suggestions shown to the player while
	 * typing a message.
	 * <p>
	 * If completions were set previously, this method will remove them all and
	 * replace them with the provided completions.
	 *
	 * @param completions the completions to set
	 */
	@Override
	public void setCustomChatCompletions(@NotNull Collection<String> completions) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Forces an update of the player's entire inventory.
	 *
	 * @apiNote It should not be necessary for plugins to use this method. If it
	 * is required for some reason, it is probably a bug.
	 */
	@Override
	public void updateInventory() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets this player's previous {@link GameMode}
	 *
	 * @return Previous game mode or null
	 */
	@Override
	public @Nullable GameMode getPreviousGameMode() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the current time on the player's client. When relative is true the
	 * player's time will be kept synchronized to its world time with the
	 * specified offset.
	 * <p>
	 * When using non relative time the player's time will stay fixed at the
	 * specified time parameter. It's up to the caller to continue updating
	 * the player's time. To restore player time to normal use
	 * resetPlayerTime().
	 *
	 * @param time     The current player's perceived time or the player's time
	 *                 offset from the server time.
	 * @param relative When true the player time is kept relative to its world
	 *                 time.
	 */
	@Override
	public void setPlayerTime(long time, boolean relative) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the player's current timestamp.
	 *
	 * @return The player's time
	 */
	@Override
	public long getPlayerTime() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the player's current time offset relative to server time, or
	 * the current player's fixed time if the player's time is absolute.
	 *
	 * @return The player's time
	 */
	@Override
	public long getPlayerTimeOffset() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns true if the player's time is relative to the server time,
	 * otherwise the player's time is absolute and will not change its current
	 * time unless done so with setPlayerTime().
	 *
	 * @return true if the player's time is relative to the server time.
	 */
	@Override
	public boolean isPlayerTimeRelative() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Restores the normal condition where the player's time is synchronized
	 * with the server time.
	 * <p>
	 * Equivalent to calling setPlayerTime(0, true).
	 */
	@Override
	public void resetPlayerTime() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the type of weather the player will see.  When used, the weather
	 * status of the player is locked until {@link #resetPlayerWeather()} is
	 * used.
	 *
	 * @param type The WeatherType enum type the player should experience
	 */
	@Override
	public void setPlayerWeather(@NotNull WeatherType type) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the type of weather the player is currently experiencing.
	 *
	 * @return The WeatherType that the player is currently experiencing or
	 * null if player is seeing server weather.
	 */
	@Override
	public @Nullable WeatherType getPlayerWeather() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Restores the normal condition where the player's weather is controlled
	 * by server conditions.
	 */
	@Override
	public void resetPlayerWeather() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player's cooldown between picking up experience orbs.
	 *
	 * @return The cooldown in ticks
	 */
	@Override
	public int getExpCooldown() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the player's cooldown between picking up experience orbs..
	 *
	 * <strong>Note:</strong> Setting this to 0 allows the player to pick up
	 * instantly, but setting this to a negative value will cause the player to
	 * be unable to pick up xp-orbs.
	 * <p>
	 * Calling this Method will result in {@link PlayerExpCooldownChangeEvent}
	 * being called.
	 *
	 * @param ticks The cooldown in ticks
	 */
	@Override
	public void setExpCooldown(int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gives the player the amount of experience specified.
	 *
	 * @param amount Exp amount to give
	 */
	@Override
	public void giveExp(int amount) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gives the player the amount of experience levels specified. Levels can
	 * be taken by specifying a negative amount.
	 *
	 * @param amount amount of experience levels to give or take
	 */
	@Override
	public void giveExpLevels(int amount) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the players current experience points towards the next level.
	 * <p>
	 * This is a percentage value. 0 is "no progress" and 1 is "next level".
	 *
	 * @return Current experience points
	 */
	@Override
	public float getExp() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the players current experience points towards the next level
	 * <p>
	 * This is a percentage value. 0 is "no progress" and 1 is "next level".
	 *
	 * @param exp New experience points
	 */
	@Override
	public void setExp(float exp) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the players current experience level
	 *
	 * @return Current experience level
	 */
	@Override
	public int getLevel() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the players current experience level
	 *
	 * @param level New experience level
	 */
	@Override
	public void setLevel(int level) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the players total experience points.
	 * <br>
	 * This refers to the total amount of experience the player has collected
	 * over time and is not currently displayed to the client.
	 *
	 * @return Current total experience points
	 */
	@Override
	public int getTotalExperience() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the players current experience points.
	 * <br>
	 * This refers to the total amount of experience the player has collected
	 * over time and is not currently displayed to the client.
	 *
	 * @param exp New total experience points
	 */
	@Override
	public void setTotalExperience(int exp) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send an experience change.
	 * <p>
	 * This fakes an experience change packet for a user. This will not actually
	 * change the experience points in any way.
	 *
	 * @param progress Experience progress percentage (between 0.0 and 1.0)
	 * @see #setExp(float)
	 */
	@Override
	public void sendExperienceChange(float progress) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send an experience change.
	 * <p>
	 * This fakes an experience change packet for a user. This will not actually
	 * change the experience points in any way.
	 *
	 * @param progress New experience progress percentage (between 0.0 and 1.0)
	 * @param level    New experience level
	 * @see #setExp(float)
	 * @see #setLevel(int)
	 */
	@Override
	public void sendExperienceChange(float progress, int level) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Determines if the Player is allowed to fly via jump key double-tap like
	 * in creative mode.
	 *
	 * @return True if the player is allowed to fly.
	 */
	@Override
	public boolean getAllowFlight() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets if the Player is allowed to fly via jump key double-tap like in
	 * creative mode.
	 *
	 * @param flight If flight should be allowed.
	 */
	@Override
	public void setAllowFlight(boolean flight) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Hides a player from this player
	 *
	 * @param player Player to hide
	 * @deprecated see {@link #hidePlayer(Plugin, Player)}
	 */
	@Override
	public void hidePlayer(@NotNull Player player) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Hides a player from this player
	 *
	 * @param plugin Plugin that wants to hide the player
	 * @param player Player to hide
	 */
	@Override
	public void hidePlayer(@NotNull Plugin plugin, @NotNull Player player) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Allows this player to see a player that was previously hidden
	 *
	 * @param player Player to show
	 * @deprecated see {@link #showPlayer(Plugin, Player)}
	 */
	@Override
	public void showPlayer(@NotNull Player player) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Allows this player to see a player that was previously hidden. If
	 * another another plugin had hidden the player too, then the player will
	 * remain hidden until the other plugin calls this method too.
	 *
	 * @param plugin Plugin that wants to show the player
	 * @param player Player to show
	 */
	@Override
	public void showPlayer(@NotNull Plugin plugin, @NotNull Player player) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks to see if a player has been hidden from this player
	 *
	 * @param player Player to check
	 * @return True if the provided player is not being hidden from this
	 * player
	 */
	@Override
	public boolean canSee(@NotNull Player player) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Visually hides an entity from this player.
	 *
	 * @param plugin Plugin that wants to hide the entity
	 * @param entity Entity to hide
	 */
	@Override
	public void hideEntity(@NotNull Plugin plugin, @NotNull Entity entity) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Allows this player to see an entity that was previously hidden. If
	 * another another plugin had hidden the entity too, then the entity will
	 * remain hidden until the other plugin calls this method too.
	 *
	 * @param plugin Plugin that wants to show the entity
	 * @param entity Entity to show
	 */
	@Override
	public void showEntity(@NotNull Plugin plugin, @NotNull Entity entity) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks to see if an entity has been visually hidden from this player.
	 *
	 * @param entity Entity to check
	 * @return True if the provided entity is not being hidden from this
	 * player
	 */
	@Override
	public boolean canSee(@NotNull Entity entity) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks to see if this player is currently flying or not.
	 *
	 * @return True if the player is flying, else false.
	 */
	@Override
	public boolean isFlying() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Makes this player start or stop flying.
	 *
	 * @param value True to fly.
	 */
	@Override
	public void setFlying(boolean value) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the speed at which a client will fly. Negative values indicate
	 * reverse directions.
	 *
	 * @param value The new speed, from -1 to 1.
	 * @throws IllegalArgumentException If new speed is less than -1 or
	 *                                  greater than 1
	 */
	@Override
	public void setFlySpeed(float value) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the speed at which a client will walk. Negative values indicate
	 * reverse directions.
	 *
	 * @param value The new speed, from -1 to 1.
	 * @throws IllegalArgumentException If new speed is less than -1 or
	 *                                  greater than 1
	 */
	@Override
	public void setWalkSpeed(float value) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the current allowed speed that a client can fly.
	 *
	 * @return The current allowed speed, from -1 to 1
	 */
	@Override
	public float getFlySpeed() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the current allowed speed that a client can walk.
	 *
	 * @return The current allowed speed, from -1 to 1
	 */
	@Override
	public float getWalkSpeed() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Request that the player's client download and switch texture packs.
	 * <p>
	 * The player's client will download the new texture pack asynchronously
	 * in the background, and will automatically switch to it once the
	 * download is complete. If the client has downloaded and cached the same
	 * texture pack in the past, it will perform a file size check against
	 * the response content to determine if the texture pack has changed and
	 * needs to be downloaded again. When this request is sent for the very
	 * first time from a given server, the client will first display a
	 * confirmation GUI to the player before proceeding with the download.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>Players can disable server textures on their client, in which
	 *     case this method will have no affect on them. Use the
	 *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
	 *     the player loaded the pack!
	 * <li>There is no concept of resetting texture packs back to default
	 *     within Minecraft, so players will have to relog to do so or you
	 *     have to send an empty pack.
	 * <li>The request is send with "null" as the hash. This might result
	 *     in newer versions not loading the pack correctly.
	 * </ul>
	 *
	 * @param url The URL from which the client will download the texture
	 *            pack. The string must contain only US-ASCII characters and should
	 *            be encoded as per RFC 1738.
	 * @throws IllegalArgumentException Thrown if the URL is null.
	 * @throws IllegalArgumentException Thrown if the URL is too long.
	 * @deprecated Minecraft no longer uses textures packs. Instead you
	 * should use {@link #setResourcePack(String)}.
	 */
	@Override
	public void setTexturePack(@NotNull String url) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Request that the player's client download and switch resource packs.
	 * <p>
	 * The player's client will download the new resource pack asynchronously
	 * in the background, and will automatically switch to it once the
	 * download is complete. If the client has downloaded and cached the same
	 * resource pack in the past, it will perform a file size check against
	 * the response content to determine if the resource pack has changed and
	 * needs to be downloaded again. When this request is sent for the very
	 * first time from a given server, the client will first display a
	 * confirmation GUI to the player before proceeding with the download.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>Players can disable server resources on their client, in which
	 *     case this method will have no affect on them. Use the
	 *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
	 *     the player loaded the pack!
	 * <li>There is no concept of resetting resource packs back to default
	 *     within Minecraft, so players will have to relog to do so or you
	 *     have to send an empty pack.
	 * <li>The request is send with empty string as the hash. This might result
	 *     in newer versions not loading the pack correctly.
	 * </ul>
	 *
	 * @param url The URL from which the client will download the resource
	 *            pack. The string must contain only US-ASCII characters and should
	 *            be encoded as per RFC 1738.
	 * @throws IllegalArgumentException Thrown if the URL is null.
	 * @throws IllegalArgumentException Thrown if the URL is too long. The
	 *                                  length restriction is an implementation specific arbitrary value.
	 */
	@Override
	public void setResourcePack(@NotNull String url) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Request that the player's client download and switch resource packs.
	 * <p>
	 * The player's client will download the new resource pack asynchronously
	 * in the background, and will automatically switch to it once the
	 * download is complete. If the client has downloaded and cached a
	 * resource pack with the same hash in the past it will not download but
	 * directly apply the cached pack. If the hash is null and the client has
	 * downloaded and cached the same resource pack in the past, it will
	 * perform a file size check against the response content to determine if
	 * the resource pack has changed and needs to be downloaded again. When
	 * this request is sent for the very first time from a given server, the
	 * client will first display a confirmation GUI to the player before
	 * proceeding with the download.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>Players can disable server resources on their client, in which
	 *     case this method will have no affect on them. Use the
	 *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
	 *     the player loaded the pack!
	 * <li>There is no concept of resetting resource packs back to default
	 *     within Minecraft, so players will have to relog to do so or you
	 *     have to send an empty pack.
	 * <li>The request is sent with empty string as the hash when the hash is
	 *     not provided. This might result in newer versions not loading the
	 *     pack correctly.
	 * </ul>
	 *
	 * @param url  The URL from which the client will download the resource
	 *             pack. The string must contain only US-ASCII characters and should
	 *             be encoded as per RFC 1738.
	 * @param hash The sha1 hash sum of the resource pack file which is used
	 *             to apply a cached version of the pack directly without downloading
	 *             if it is available. Hast to be 20 bytes long!
	 * @throws IllegalArgumentException Thrown if the URL is null.
	 * @throws IllegalArgumentException Thrown if the URL is too long. The
	 *                                  length restriction is an implementation specific arbitrary value.
	 * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
	 *                                  long.
	 */
	@Override
	public void setResourcePack(@NotNull String url, byte @Nullable [] hash) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Request that the player's client download and switch resource packs.
	 * <p>
	 * The player's client will download the new resource pack asynchronously
	 * in the background, and will automatically switch to it once the
	 * download is complete. If the client has downloaded and cached a
	 * resource pack with the same hash in the past it will not download but
	 * directly apply the cached pack. If the hash is null and the client has
	 * downloaded and cached the same resource pack in the past, it will
	 * perform a file size check against the response content to determine if
	 * the resource pack has changed and needs to be downloaded again. When
	 * this request is sent for the very first time from a given server, the
	 * client will first display a confirmation GUI to the player before
	 * proceeding with the download.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>Players can disable server resources on their client, in which
	 *     case this method will have no affect on them. Use the
	 *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
	 *     the player loaded the pack!
	 * <li>To remove a resource pack you can use
	 *     {@link #removeResourcePack(UUID)} or {@link #removeResourcePacks()}.
	 * <li>The request is sent with empty string as the hash when the hash is
	 *     not provided. This might result in newer versions not loading the
	 *     pack correctly.
	 * </ul>
	 *
	 * @param url    The URL from which the client will download the resource
	 *               pack. The string must contain only US-ASCII characters and should
	 *               be encoded as per RFC 1738.
	 * @param hash   The sha1 hash sum of the resource pack file which is used
	 *               to apply a cached version of the pack directly without downloading
	 *               if it is available. Hast to be 20 bytes long!
	 * @param prompt The optional custom prompt message to be shown to client.
	 * @throws IllegalArgumentException Thrown if the URL is null.
	 * @throws IllegalArgumentException Thrown if the URL is too long. The
	 *                                  length restriction is an implementation specific arbitrary value.
	 * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
	 *                                  long.
	 */
	@Override
	public void setResourcePack(@NotNull String url, byte @Nullable [] hash, @Nullable String prompt) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Request that the player's client download and switch resource packs.
	 * <p>
	 * The player's client will download the new resource pack asynchronously
	 * in the background, and will automatically switch to it once the
	 * download is complete. If the client has downloaded and cached a
	 * resource pack with the same hash in the past it will not download but
	 * directly apply the cached pack. If the hash is null and the client has
	 * downloaded and cached the same resource pack in the past, it will
	 * perform a file size check against the response content to determine if
	 * the resource pack has changed and needs to be downloaded again. When
	 * this request is sent for the very first time from a given server, the
	 * client will first display a confirmation GUI to the player before
	 * proceeding with the download.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>Players can disable server resources on their client, in which
	 *     case this method will have no affect on them. Use the
	 *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
	 *     the player loaded the pack!
	 * <li>To remove a resource pack you can use
	 *     {@link #removeResourcePack(UUID)} or {@link #removeResourcePacks()}.
	 * <li>The request is sent with empty string as the hash when the hash is
	 *     not provided. This might result in newer versions not loading the
	 *     pack correctly.
	 * </ul>
	 *
	 * @param url   The URL from which the client will download the resource
	 *              pack. The string must contain only US-ASCII characters and should
	 *              be encoded as per RFC 1738.
	 * @param hash  The sha1 hash sum of the resource pack file which is used
	 *              to apply a cached version of the pack directly without downloading
	 *              if it is available. Hast to be 20 bytes long!
	 * @param force If true, the client will be disconnected from the server
	 *              when it declines to use the resource pack.
	 * @throws IllegalArgumentException Thrown if the URL is null.
	 * @throws IllegalArgumentException Thrown if the URL is too long. The
	 *                                  length restriction is an implementation specific arbitrary value.
	 * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
	 *                                  long.
	 */
	@Override
	public void setResourcePack(@NotNull String url, byte @Nullable [] hash, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Request that the player's client download and switch resource packs.
	 * <p>
	 * The player's client will download the new resource pack asynchronously
	 * in the background, and will automatically switch to it once the
	 * download is complete. If the client has downloaded and cached a
	 * resource pack with the same hash in the past it will not download but
	 * directly apply the cached pack. If the hash is null and the client has
	 * downloaded and cached the same resource pack in the past, it will
	 * perform a file size check against the response content to determine if
	 * the resource pack has changed and needs to be downloaded again. When
	 * this request is sent for the very first time from a given server, the
	 * client will first display a confirmation GUI to the player before
	 * proceeding with the download.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>Players can disable server resources on their client, in which
	 *     case this method will have no affect on them. Use the
	 *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
	 *     the player loaded the pack!
	 * <li>To remove a resource pack you can use
	 *     {@link #removeResourcePack(UUID)} or {@link #removeResourcePacks()}.
	 * <li>The request is sent with empty string as the hash when the hash is
	 *     not provided. This might result in newer versions not loading the
	 *     pack correctly.
	 * </ul>
	 *
	 * @param url    The URL from which the client will download the resource
	 *               pack. The string must contain only US-ASCII characters and should
	 *               be encoded as per RFC 1738.
	 * @param hash   The sha1 hash sum of the resource pack file which is used
	 *               to apply a cached version of the pack directly without downloading
	 *               if it is available. Hast to be 20 bytes long!
	 * @param prompt The optional custom prompt message to be shown to client.
	 * @param force  If true, the client will be disconnected from the server
	 *               when it declines to use the resource pack.
	 * @throws IllegalArgumentException Thrown if the URL is null.
	 * @throws IllegalArgumentException Thrown if the URL is too long. The
	 *                                  length restriction is an implementation specific arbitrary value.
	 * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
	 *                                  long.
	 */
	@Override
	public void setResourcePack(@NotNull String url, byte @Nullable [] hash, @Nullable String prompt, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Request that the player's client download and switch resource packs.
	 * <p>
	 * The player's client will download the new resource pack asynchronously
	 * in the background, and will automatically switch to it once the
	 * download is complete. If the client has downloaded and cached a
	 * resource pack with the same hash in the past it will not download but
	 * directly apply the cached pack. If the hash is null and the client has
	 * downloaded and cached the same resource pack in the past, it will
	 * perform a file size check against the response content to determine if
	 * the resource pack has changed and needs to be downloaded again. When
	 * this request is sent for the very first time from a given server, the
	 * client will first display a confirmation GUI to the player before
	 * proceeding with the download.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>Players can disable server resources on their client, in which
	 *     case this method will have no affect on them. Use the
	 *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
	 *     the player loaded the pack!
	 * <li>To remove a resource pack you can use
	 *     {@link #removeResourcePack(UUID)} or {@link #removeResourcePacks()}.
	 * <li>The request is sent with empty string as the hash when the hash is
	 *     not provided. This might result in newer versions not loading the
	 *     pack correctly.
	 * </ul>
	 *
	 * @param id     Unique resource pack ID.
	 * @param url    The URL from which the client will download the resource
	 *               pack. The string must contain only US-ASCII characters and should
	 *               be encoded as per RFC 1738.
	 * @param hash   The sha1 hash sum of the resource pack file which is used
	 *               to apply a cached version of the pack directly without downloading
	 *               if it is available. Hast to be 20 bytes long!
	 * @param prompt The optional custom prompt message to be shown to client.
	 * @param force  If true, the client will be disconnected from the server
	 *               when it declines to use the resource pack.
	 * @throws IllegalArgumentException Thrown if the URL is null.
	 * @throws IllegalArgumentException Thrown if the URL is too long. The
	 *                                  length restriction is an implementation specific arbitrary value.
	 * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
	 *                                  long.
	 */
	@Override
	public void setResourcePack(@NotNull UUID id, @NotNull String url, byte @Nullable [] hash,
	                            @Nullable String prompt, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Request that the player's client download and include another resource pack.
	 * <p>
	 * The player's client will download the new resource pack asynchronously
	 * in the background, and will automatically add to it once the
	 * download is complete. If the client has downloaded and cached a
	 * resource pack with the same hash in the past it will not download but
	 * directly apply the cached pack. If the hash is null and the client has
	 * downloaded and cached the same resource pack in the past, it will
	 * perform a file size check against the response content to determine if
	 * the resource pack has changed and needs to be downloaded again. When
	 * this request is sent for the very first time from a given server, the
	 * client will first display a confirmation GUI to the player before
	 * proceeding with the download.
	 * <p>
	 * Notes:
	 * <ul>
	 * <li>Players can disable server resources on their client, in which
	 *     case this method will have no affect on them. Use the
	 *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
	 *     the player loaded the pack!
	 * <li>To remove a resource pack you can use
	 *     {@link #removeResourcePack(UUID)} or {@link #removeResourcePacks()}.
	 * <li>The request is sent with empty string as the hash when the hash is
	 *     not provided. This might result in newer versions not loading the
	 *     pack correctly.
	 * </ul>
	 *
	 * @param id     Unique resource pack ID.
	 * @param url    The URL from which the client will download the resource
	 *               pack. The string must contain only US-ASCII characters and should
	 *               be encoded as per RFC 1738.
	 * @param hash   The sha1 hash sum of the resource pack file which is used
	 *               to apply a cached version of the pack directly without downloading
	 *               if it is available. Hast to be 20 bytes long!
	 * @param prompt The optional custom prompt message to be shown to client.
	 * @param force  If true, the client will be disconnected from the server
	 *               when it declines to use the resource pack.
	 * @throws IllegalArgumentException Thrown if the URL is null.
	 * @throws IllegalArgumentException Thrown if the URL is too long. The
	 *                                  length restriction is an implementation specific arbitrary value.
	 * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
	 *                                  long.
	 */
	@Override
	public void addResourcePack(@NotNull UUID id, @NotNull String url, byte @Nullable [] hash,
	                            @Nullable String prompt, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Request that the player's client remove a resource pack sent by the
	 * server.
	 *
	 * @param id the id of the resource pack.
	 * @throws IllegalArgumentException If the ID is null.
	 */
	@Override
	public void removeResourcePack(@NotNull UUID id) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Request that the player's client remove all loaded resource pack sent by
	 * the server.
	 */
	@Override
	public void removeResourcePacks() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the Scoreboard displayed to this player
	 *
	 * @return The current scoreboard seen by this player
	 */
	@Override
	public @NotNull Scoreboard getScoreboard() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the player's visible Scoreboard.
	 *
	 * @param scoreboard New Scoreboard for the player
	 * @throws IllegalArgumentException if scoreboard is null
	 * @throws IllegalArgumentException if scoreboard was not created by the
	 *                                  {@link ScoreboardManager scoreboard manager}
	 * @throws IllegalStateException    if this is a player that is not logged
	 *                                  yet or has logged out
	 */
	@Override
	public void setScoreboard(@NotNull Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the {@link WorldBorder} visible to this Player, or null if viewing
	 * the world's world border.
	 *
	 * @return the player's world border
	 */
	@Override
	public @Nullable WorldBorder getWorldBorder() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the {@link WorldBorder} visible to this Player.
	 *
	 * @param border the border to set, or null to set to the world border of
	 *               the player's current world
	 * @throws UnsupportedOperationException if setting the border to that of
	 *                                       a world in which the player is not currently present.
	 * @see Server#createWorldBorder()
	 */
	@Override
	public void setWorldBorder(@Nullable WorldBorder border) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send a health update to the player. This will adjust the health, food, and
	 * saturation on the client and will not affect the player's actual values on
	 * the server. As soon as any of these values change on the server, changes sent
	 * by this method will no longer be visible.
	 *
	 * @param health     the health. If 0.0, the client will believe it is dead
	 * @param foodLevel  the food level
	 * @param saturation the saturation
	 */
	@Override
	public void sendHealthUpdate(double health, int foodLevel, float saturation) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Send a health update to the player using its known server values. This will
	 * synchronize the health, food, and saturation on the client and therefore may
	 * be useful when changing a player's maximum health attribute.
	 */
	@Override
	public void sendHealthUpdate() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets if the client is displayed a 'scaled' health, that is, health on a
	 * scale from 0-{@link #getHealthScale()}.
	 *
	 * @return if client health display is scaled
	 * @see Player#setHealthScaled(boolean)
	 */
	@Override
	public boolean isHealthScaled() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets if the client is displayed a 'scaled' health, that is, health on a
	 * scale from 0-{@link #getHealthScale()}.
	 * <p>
	 * Displayed health follows a simple formula <code>displayedHealth =
	 * getHealth() / getMaxHealth() * getHealthScale()</code>.
	 *
	 * @param scale if the client health display is scaled
	 */
	@Override
	public void setHealthScaled(boolean scale) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the number to scale health to for the client; this will also
	 * {@link #setHealthScaled(boolean) setHealthScaled(true)}.
	 * <p>
	 * Displayed health follows a simple formula <code>displayedHealth =
	 * getHealth() / getMaxHealth() * getHealthScale()</code>.
	 *
	 * @param scale the number to scale health to
	 * @throws IllegalArgumentException if scale is &lt;0
	 * @throws IllegalArgumentException if scale is {@link Double#NaN}
	 * @throws IllegalArgumentException if scale is too high
	 */
	@Override
	public void setHealthScale(double scale) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the number that health is scaled to for the client.
	 *
	 * @return the number that health would be scaled to for the client if
	 * HealthScaling is set to true
	 * @see Player#setHealthScale(double)
	 * @see Player#setHealthScaled(boolean)
	 */
	@Override
	public double getHealthScale() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the entity which is followed by the camera when in
	 * {@link GameMode#SPECTATOR}.
	 *
	 * @return the followed entity, or null if not in spectator mode or not
	 * following a specific entity.
	 */
	@Override
	public @Nullable Entity getSpectatorTarget() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the entity which is followed by the camera when in
	 * {@link GameMode#SPECTATOR}.
	 *
	 * @param entity the entity to follow or null to reset
	 * @throws IllegalStateException if the player is not in
	 *                               {@link GameMode#SPECTATOR}
	 */
	@Override
	public void setSpectatorTarget(@Nullable Entity entity) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sends a title and a subtitle message to the player. If either of these
	 * values are null, they will not be sent and the display will remain
	 * unchanged. If they are empty strings, the display will be updated as
	 * such. If the strings contain a new line, only the first line will be
	 * sent. The titles will be displayed with the client's default timings.
	 *
	 * @param title    Title text
	 * @param subtitle Subtitle text
	 * @deprecated API behavior subject to change
	 */
	@Override
	public void sendTitle(@Nullable String title, @Nullable String subtitle) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sends a title and a subtitle message to the player. If either of these
	 * values are null, they will not be sent and the display will remain
	 * unchanged. If they are empty strings, the display will be updated as
	 * such. If the strings contain a new line, only the first line will be
	 * sent. All timings values may take a value of -1 to indicate that they
	 * will use the last value sent (or the defaults if no title has been
	 * displayed).
	 *
	 * @param title    Title text
	 * @param subtitle Subtitle text
	 * @param fadeIn   time in ticks for titles to fade in. Defaults to 10.
	 * @param stay     time in ticks for titles to stay. Defaults to 70.
	 * @param fadeOut  time in ticks for titles to fade out. Defaults to 20.
	 */
	@Override
	public void sendTitle(@Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Resets the title displayed to the player. This will clear the displayed
	 * title / subtitle and reset timings to their default values.
	 */
	@Override
	public void resetTitle() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location.
	 *
	 * @param particle the particle to spawn
	 * @param location the location to spawn at
	 * @param count    the number of particles
	 */
	@Override
	public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location.
	 *
	 * @param particle the particle to spawn
	 * @param x        the position on the x axis to spawn at
	 * @param y        the position on the y axis to spawn at
	 * @param z        the position on the z axis to spawn at
	 * @param count    the number of particles
	 */
	@Override
	public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location.
	 *
	 * @param particle the particle to spawn
	 * @param location the location to spawn at
	 * @param count    the number of particles
	 * @param data     the data to use for the particle or null,
	 *                 the type of this depends on {@link Particle#getDataType()}
	 */
	@Override
	public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location,
	                              int count, @Nullable T data) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location.
	 *
	 * @param particle the particle to spawn
	 * @param x        the position on the x axis to spawn at
	 * @param y        the position on the y axis to spawn at
	 * @param z        the position on the z axis to spawn at
	 * @param count    the number of particles
	 * @param data     the data to use for the particle or null,
	 *                 the type of this depends on {@link Particle#getDataType()}
	 */
	@Override
	public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z,
	                              int count, @Nullable T data) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location. The position of each particle will be
	 * randomized positively and negatively by the offset parameters
	 * on each axis.
	 *
	 * @param particle the particle to spawn
	 * @param location the location to spawn at
	 * @param count    the number of particles
	 * @param offsetX  the maximum random offset on the X axis
	 * @param offsetY  the maximum random offset on the Y axis
	 * @param offsetZ  the maximum random offset on the Z axis
	 */
	@Override
	public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count,
	                          double offsetX, double offsetY, double offsetZ) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location. The position of each particle will be
	 * randomized positively and negatively by the offset parameters
	 * on each axis.
	 *
	 * @param particle the particle to spawn
	 * @param x        the position on the x axis to spawn at
	 * @param y        the position on the y axis to spawn at
	 * @param z        the position on the z axis to spawn at
	 * @param count    the number of particles
	 * @param offsetX  the maximum random offset on the X axis
	 * @param offsetY  the maximum random offset on the Y axis
	 * @param offsetZ  the maximum random offset on the Z axis
	 */
	@Override
	public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count,
	                          double offsetX, double offsetY, double offsetZ) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location. The position of each particle will be
	 * randomized positively and negatively by the offset parameters
	 * on each axis.
	 *
	 * @param particle the particle to spawn
	 * @param location the location to spawn at
	 * @param count    the number of particles
	 * @param offsetX  the maximum random offset on the X axis
	 * @param offsetY  the maximum random offset on the Y axis
	 * @param offsetZ  the maximum random offset on the Z axis
	 * @param data     the data to use for the particle or null,
	 *                 the type of this depends on {@link Particle#getDataType()}
	 */
	@Override
	public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count,
	                              double offsetX, double offsetY, double offsetZ, @Nullable T data) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location. The position of each particle will be
	 * randomized positively and negatively by the offset parameters
	 * on each axis.
	 *
	 * @param particle the particle to spawn
	 * @param x        the position on the x axis to spawn at
	 * @param y        the position on the y axis to spawn at
	 * @param z        the position on the z axis to spawn at
	 * @param count    the number of particles
	 * @param offsetX  the maximum random offset on the X axis
	 * @param offsetY  the maximum random offset on the Y axis
	 * @param offsetZ  the maximum random offset on the Z axis
	 * @param data     the data to use for the particle or null,
	 *                 the type of this depends on {@link Particle#getDataType()}
	 */
	@Override
	public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count,
	                              double offsetX, double offsetY, double offsetZ, @Nullable T data) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location. The position of each particle will be
	 * randomized positively and negatively by the offset parameters
	 * on each axis.
	 *
	 * @param particle the particle to spawn
	 * @param location the location to spawn at
	 * @param count    the number of particles
	 * @param offsetX  the maximum random offset on the X axis
	 * @param offsetY  the maximum random offset on the Y axis
	 * @param offsetZ  the maximum random offset on the Z axis
	 * @param extra    the extra data for this particle, depends on the
	 *                 particle used (normally speed)
	 */
	@Override
	public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count,
	                          double offsetX, double offsetY, double offsetZ, double extra) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location. The position of each particle will be
	 * randomized positively and negatively by the offset parameters
	 * on each axis.
	 *
	 * @param particle the particle to spawn
	 * @param x        the position on the x axis to spawn at
	 * @param y        the position on the y axis to spawn at
	 * @param z        the position on the z axis to spawn at
	 * @param count    the number of particles
	 * @param offsetX  the maximum random offset on the X axis
	 * @param offsetY  the maximum random offset on the Y axis
	 * @param offsetZ  the maximum random offset on the Z axis
	 * @param extra    the extra data for this particle, depends on the
	 *                 particle used (normally speed)
	 */
	@Override
	public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count,
	                          double offsetX, double offsetY, double offsetZ, double extra) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location. The position of each particle will be
	 * randomized positively and negatively by the offset parameters
	 * on each axis.
	 *
	 * @param particle the particle to spawn
	 * @param location the location to spawn at
	 * @param count    the number of particles
	 * @param offsetX  the maximum random offset on the X axis
	 * @param offsetY  the maximum random offset on the Y axis
	 * @param offsetZ  the maximum random offset on the Z axis
	 * @param extra    the extra data for this particle, depends on the
	 *                 particle used (normally speed)
	 * @param data     the data to use for the particle or null,
	 *                 the type of this depends on {@link Particle#getDataType()}
	 */
	@Override
	public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count,
	                              double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Spawns the particle (the number of times specified by count)
	 * at the target location. The position of each particle will be
	 * randomized positively and negatively by the offset parameters
	 * on each axis.
	 *
	 * @param particle the particle to spawn
	 * @param x        the position on the x axis to spawn at
	 * @param y        the position on the y axis to spawn at
	 * @param z        the position on the z axis to spawn at
	 * @param count    the number of particles
	 * @param offsetX  the maximum random offset on the X axis
	 * @param offsetY  the maximum random offset on the Y axis
	 * @param offsetZ  the maximum random offset on the Z axis
	 * @param extra    the extra data for this particle, depends on the
	 *                 particle used (normally speed)
	 * @param data     the data to use for the particle or null,
	 *                 the type of this depends on {@link Particle#getDataType()}
	 */
	@Override
	public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count,
	                              double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Return the player's progression on the specified advancement.
	 *
	 * @param advancement advancement
	 * @return object detailing the player's progress
	 */
	@Override
	public @NotNull AdvancementProgress getAdvancementProgress(@NotNull Advancement advancement) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the player's current client side view distance.
	 * <br>
	 * Will default to the server view distance if the client has not yet
	 * communicated this information,
	 *
	 * @return client view distance as above
	 */
	@Override
	public int getClientViewDistance() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player's estimated ping in milliseconds.
	 * <p>
	 * In Vanilla this value represents a weighted average of the response time
	 * to application layer ping packets sent. This value does not represent the
	 * network round trip time and as such may have less granularity and be
	 * impacted by other sources. For these reasons it <b>should not</b> be used
	 * for anti-cheat purposes. Its recommended use is only as a
	 * <b>qualitative</b> indicator of connection quality (Vanilla uses it for
	 * this purpose in the tab list).
	 *
	 * @return player ping
	 */
	@Override
	public int getPing() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player's current locale.
	 * <p>
	 * The value of the locale String is not defined properly.
	 * <br>
	 * The vanilla Minecraft client will use lowercase language / country pairs
	 * separated by an underscore, but custom resource packs may use any format
	 * they wish.
	 *
	 * @return the player's locale
	 */
	@Override
	public @NotNull String getLocale() {
		return this.configuration.getLocale();
	}

	/**
	 * Update the list of commands sent to the client.
	 * <br>
	 * Generally useful to ensure the client has a complete list of commands
	 * after permission changes are done.
	 */
	@Override
	public void updateCommands() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Open a {@link Material#WRITTEN_BOOK} for a Player
	 *
	 * @param book The book to open for this player
	 */
	@Override
	public void openBook(@NotNull ItemStack book) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Open a Sign for editing by the Player.
	 * <p>
	 * The Sign must be placed in the same world as the player.
	 *
	 * @param sign The sign to edit
	 */
	@Override
	public void openSign(@NotNull Sign sign) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Open a Sign for editing by the Player.
	 * <p>
	 * The Sign must be placed in the same world as the player.
	 *
	 * @param sign The sign to edit
	 * @param side The side to edit
	 */
	@Override
	public void openSign(@NotNull Sign sign, @NotNull Side side) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Shows the demo screen to the player, this screen is normally only seen in
	 * the demo version of the game.
	 * <br>
	 * Servers can modify the text on this screen using a resource pack.
	 */
	@Override
	public void showDemoScreen() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the player has the "Allow Server Listings" setting enabled.
	 *
	 * @return whether the player allows server listings
	 */
	@Override
	public boolean isAllowingServerListings() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sends this recipient a Plugin Message on the specified outgoing
	 * channel.
	 * <p>
	 * The message may not be larger than {@link Messenger#MAX_MESSAGE_SIZE}
	 * bytes, and the plugin must be registered to send messages on the
	 * specified channel.
	 *
	 * @param source  The plugin that sent this message.
	 * @param channel The channel to send this message on.
	 * @param message The raw message to send.
	 * @throws IllegalArgumentException      Thrown if the source plugin is
	 *                                       disabled.
	 * @throws IllegalArgumentException      Thrown if source, channel or message
	 *                                       is null.
	 * @throws MessageTooLargeException      Thrown if the message is too big.
	 * @throws ChannelNotRegisteredException Thrown if the channel is not
	 *                                       registered for this plugin.
	 */
	@Override
	public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, byte @NotNull [] message) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a set containing all the Plugin Channels that this client is
	 * listening on.
	 *
	 * @return Set containing all the channels that this client may accept.
	 */
	@Override
	public @NotNull Set<String> getListeningPluginChannels() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public @NotNull Player.Spigot spigot() {
		return new PrismarinePlayer.Spigot();
	}

	public static class Spigot extends Player.Spigot {

	}

	@Override
	public String toString() {
		return "PrismarinePlayer{name=" + this.getName() + ",location=" + this.getLocation() + "}";
	}
}