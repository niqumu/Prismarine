package app.prismarine.server.player;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PrismarinePlayerProfile implements PlayerProfile {

	@Getter
	private String name;

	@Getter
	private UUID uuid;

	@Getter
	private PlayerTextures textures;

	/**
	 * Create a new PlayerProfile from name and UUID
	 * @param name The name of the player
	 * @param uuid The UUID of the player
	 */
	public PrismarinePlayerProfile(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
		this.textures = new PrismarinePlayerTextures();
	}

	/**
	 * Gets the player's unique id.
	 *
	 * @return the player's unique id, or <code>null</code> if not available
	 */
	@Override @Nullable
	public UUID getUniqueId() {
		return this.uuid;
	}

	/**
	 * Gets the player name.
	 *
	 * @return the player name, or <code>null</code> if not available
	 */
	@Override @Nullable
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the {@link PlayerTextures} of this profile.
	 *
	 * @return the textures, not <code>null</code>
	 */
	@Override @NonNull
	public PlayerTextures getTextures() {
		return this.textures;
	}

	/**
	 * Copies the given textures.
	 *
	 * @param textures the textures to copy, or <code>null</code> to clear the
	 * textures
	 */
	@Override
	public void setTextures(@Nullable PlayerTextures textures) {
		this.textures = textures;
	}

	/**
	 * Checks whether this profile is complete.
	 * <p>
	 * A profile is currently considered complete if it has a name, a unique id,
	 * and textures.
	 *
	 * @return <code>true</code> if this profile is complete
	 */
	@Override
	public boolean isComplete() {
		return this.name != null && !this.name.isEmpty() && this.uuid != null && !this.textures.isEmpty();
	}

	/**
	 * Produces an updated player profile based on this profile.
	 * <p>
	 * This tries to produce a completed profile by filling in missing
	 * properties (name, unique id, textures, etc.), and updates existing
	 * properties (e.g. name, textures, etc.) to their official and up-to-date
	 * values. This operation does not alter the current profile, but produces a
	 * new updated {@link PlayerProfile}.
	 * <p>
	 * If no player exists for the unique id or name of this profile, this
	 * operation yields a profile that is equal to the current profile, which
	 * might not be complete.
	 * <p>
	 * This is an asynchronous operation: Updating the profile can result in an
	 * outgoing connection in another thread in order to fetch the latest
	 * profile properties. The returned {@link CompletableFuture} will be
	 * completed once the updated profile is available. In order to not block
	 * the server's main thread, you should not wait for the result of the
	 * returned CompletableFuture on the server's main thread. Instead, if you
	 * want to do something with the updated player profile on the server's main
	 * thread once it is available, you could do something like this:
	 * <pre>
	 * profile.update().thenAcceptAsync(updatedProfile -> {
	 *     // Do something with the updated profile:
	 *     // ...
	 * }, runnable -> Bukkit.getScheduler().runTask(plugin, runnable));
	 * </pre>
	 *
	 * @return a completable future that gets completed with the updated
	 * PlayerProfile once it is available
	 */
	@Override @NonNull
	public CompletableFuture<PlayerProfile> update() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * @return A copy of this player profile
	 */
	@Override @NonNull
	public PlayerProfile clone() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override @NonNull
	public Map<String, Object> serialize() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PrismarinePlayerProfile that = (PrismarinePlayerProfile) o;
		return Objects.equals(this.uuid, that.uuid); // We only care if the UUID matches; usernames change
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, uuid);
	}
}
