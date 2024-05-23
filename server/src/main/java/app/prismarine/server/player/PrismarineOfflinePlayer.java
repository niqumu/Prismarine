package app.prismarine.server.player;

import app.prismarine.server.PrismarineServer;
import org.bukkit.*;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PrismarineOfflinePlayer implements OfflinePlayer {

	private final PlayerProfile playerProfile;

	public PrismarineOfflinePlayer(PlayerProfile playerProfile) {
		this.playerProfile = playerProfile;
	}

	/**
	 * Checks if this player is currently online
	 *
	 * @return true if they are online
	 */
	@Override
	public boolean isOnline() {

		// Iterate over online players, checking for a match
		for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
			if (onlinePlayer.getUniqueId().equals(this.getUniqueId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the name of this player
	 * <p>
	 * Names are no longer unique past a single game session. For persistent storage
	 * it is recommended that you use {@link #getUniqueId()} instead.
	 *
	 * @return Player name or null if we have not seen a name for this player yet
	 */
	@Override @Nullable
	public String getName() {
		return this.playerProfile.getName();
	}

	/**
	 * Returns the UUID of this player
	 *
	 * @return Player UUID
	 */
	@Override @NotNull
	public UUID getUniqueId() {
		return Objects.requireNonNullElse(this.playerProfile.getUniqueId(), UUID.randomUUID());
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
	@Override @NotNull
	public PlayerProfile getPlayerProfile() {
		return this.playerProfile;
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
	 * @param reason reason for the ban, null indicates implementation default
	 * @param expires date for the ban's expiration (unban), or null to imply
	 *     forever
	 * @param source source of the ban, null indicates implementation default
	 * @return the entry for the newly created ban, or the entry for the
	 *     (updated) previous ban
	 */
	@Override @Nullable
	public BanEntry<PlayerProfile> ban(@Nullable String reason,
	                                   @Nullable Date expires, @Nullable String source) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
	 * update the entry.
	 *
	 * @param reason reason for the ban, null indicates implementation default
	 * @param expires instant for the ban's expiration (unban), or null to imply
	 *     forever
	 * @param source source of the ban, null indicates implementation default
	 * @return the entry for the newly created ban, or the entry for the
	 *     (updated) previous ban
	 */
	@Override
	public BanEntry<PlayerProfile> ban(@Nullable String reason,
	                                   @Nullable Instant expires, @Nullable String source) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
	 * update the entry.
	 *
	 * @param reason reason for the ban, null indicates implementation default
	 * @param duration how long the ban last, or null to imply
	 *     forever
	 * @param source source of the ban, null indicates implementation default
	 * @return the entry for the newly created ban, or the entry for the
	 *     (updated) previous ban
	 */
	@Override
	public BanEntry<PlayerProfile> ban(@Nullable String reason,
	                                   @Nullable Duration duration, @Nullable String source) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks if this player is whitelisted or not
	 *
	 * @return true if whitelisted
	 */
	@Override
	public boolean isWhitelisted() {
		return ((PrismarineServer) Bukkit.getServer()).getWhitelist().contains(this);
	}

	/**
	 * Sets if this player is whitelisted or not
	 *
	 * @param value true if whitelisted
	 */
	@Override
	public void setWhitelisted(boolean value) {
		if (value) {
			((PrismarineServer) Bukkit.getServer()).getWhitelist().add(this);
		} else {
			((PrismarineServer) Bukkit.getServer()).getWhitelist().remove(this);
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
	@Override @Nullable
	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(this.getUniqueId());
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
	 * Gets the Location where the player will spawn at their bed, null if
	 * they have not slept in one or their current bed spawn is invalid.
	 *
	 * @return Bed Spawn Location if bed exists, otherwise null.
	 *
	 * @see #getRespawnLocation()
	 * @deprecated Misleading name. This method also returns the location of
	 * respawn anchors.
	 */
	@Override @Deprecated @Nullable
	public Location getBedSpawnLocation() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the Location where the player will spawn at, null if they
	 * don't have a valid respawn point.
	 *
	 * @return respawn location if exists, otherwise null.
	 */
	@Override @Nullable
	public Location getRespawnLocation() {
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
	 *     additional parameter
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
	 *     additional parameter
	 */
	@Override
	public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Increments the given statistic for this player.
	 *
	 * @param statistic Statistic to increment
	 * @param amount Amount to increment this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the statistic requires an
	 *     additional parameter
	 */
	@Override
	public void incrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Decrements the given statistic for this player.
	 *
	 * @param statistic Statistic to decrement
	 * @param amount Amount to decrement this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the statistic requires an
	 *     additional parameter
	 */
	@Override
	public void decrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the given statistic for this player.
	 *
	 * @param statistic Statistic to set
	 * @param newValue The value to set this statistic to
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if newValue is negative
	 * @throws IllegalArgumentException if the statistic requires an
	 *     additional parameter
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
	 *     additional parameter
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
	 * @param material Material to offset the statistic with
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
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
	 * @param material Material to offset the statistic with
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
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
	 * @param material Material offset of the statistic
	 * @return the value of the given statistic
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
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
	 * @param material Material to offset the statistic with
	 * @param amount Amount to increment this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
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
	 * @param material Material to offset the statistic with
	 * @param amount Amount to decrement this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
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
	 * @param material Material to offset the statistic with
	 * @param newValue The value to set this statistic to
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if material is null
	 * @throws IllegalArgumentException if newValue is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
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
	 * @param statistic Statistic to increment
	 * @param entityType EntityType to offset the statistic with
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
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
	 * @param statistic Statistic to decrement
	 * @param entityType EntityType to offset the statistic with
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
	 */
	@Override
	public void decrementStatistic(@NotNull Statistic statistic,
	                               @NotNull EntityType entityType) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the value of the given statistic for this player.
	 *
	 * @param statistic Statistic to check
	 * @param entityType EntityType offset of the statistic
	 * @return the value of the given statistic
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
	 */
	@Override
	public int getStatistic(@NotNull Statistic statistic,
	                        @NotNull EntityType entityType) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Increments the given statistic for this player for the given entity.
	 *
	 * @param statistic Statistic to increment
	 * @param entityType EntityType to offset the statistic with
	 * @param amount Amount to increment this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
	 */
	@Override
	public void incrementStatistic(@NotNull Statistic statistic,
	                               @NotNull EntityType entityType, int amount) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Decrements the given statistic for this player for the given entity.
	 *
	 * @param statistic Statistic to decrement
	 * @param entityType EntityType to offset the statistic with
	 * @param amount Amount to decrement this statistic by
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if amount is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
	 */
	@Override
	public void decrementStatistic(@NotNull Statistic statistic,
	                               @NotNull EntityType entityType, int amount) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the given statistic for this player for the given entity.
	 *
	 * @param statistic Statistic to set
	 * @param entityType EntityType to offset the statistic with
	 * @param newValue The value to set this statistic to
	 * @throws IllegalArgumentException if statistic is null
	 * @throws IllegalArgumentException if entityType is null
	 * @throws IllegalArgumentException if newValue is negative
	 * @throws IllegalArgumentException if the given parameter is not valid
	 *     for the statistic
	 */
	@Override
	public void setStatistic(@NotNull Statistic statistic,
	                         @NotNull EntityType entityType, int newValue) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player's last death location.
	 *
	 * @return the last death location if it exists, otherwise null.
	 */
	@Override @Nullable
	public Location getLastDeathLocation() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player's current location.
	 *
	 * @return the player's location, {@code null} if player hasn't ever played
	 * before.
	 */
	@Override @Nullable
	public Location getLocation() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override @NotNull
	public Map<String, Object> serialize() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks if this object is a server operator
	 *
	 * @return true if this is an operator, otherwise false
	 */
	@Override
	public boolean isOp() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the operator status of this object
	 *
	 * @param value New operator value
	 */
	@Override
	public void setOp(boolean value) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PrismarineOfflinePlayer that) {
			return this.playerProfile.equals(that.playerProfile);
		}
		return false;
	}
}
