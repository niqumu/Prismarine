package app.prismarine.server.entity;

import app.prismarine.server.PrismarineServer;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityManager {

	/**
	 * A map of all entities, keyed by their entity ID
	 */
	@Getter
	private final Map<Integer, PrismarineEntity> entities = new ConcurrentHashMap<>();

	/**
	 * A map of all online players, keyed by their UUID
	 */
	@Getter
	private final Map<UUID, PrismarinePlayer> onlinePlayers = new ConcurrentHashMap<>();

	private final AtomicInteger lastID = new AtomicInteger(1);

	/**
	 * Gets an entity by its entity ID
	 * @param id The ID of the entity to retrieve
	 * @return The entity with the provided ID, or {@code null} if no such entity exists
	 */
	public PrismarineEntity getEntity(int id) {
		return this.entities.get(id);
	}

	/**
	 * Gets a player by their UUID
	 * @param uuid The UUID of the player to retrieve
	 * @return The player with the provided UUID, or {@code null} if no such player exists
	 */
	public PrismarinePlayer getPlayer(UUID uuid) {
		return this.onlinePlayers.get(uuid);
	}

	/**
	 * Gets a player by their name
	 * @param name The name of the player to retrieve
	 * @return The player with the provided name, or {@code null} if no such player exists
	 */
	public PrismarinePlayer getPlayer(String name) {
		for (PrismarinePlayer player : this.onlinePlayers.values()) {
			if (player.getName().equalsIgnoreCase(name)) {
				return player;
			}
		}

		return null;
	}

	/**
	 * Registers an entity into the entity list, allocating it an entity ID
	 * @param entity The entity to add to the list
	 * @return The ID that the entity was allocated
	 */
	public int register(PrismarineEntity entity) {
		int id = lastID.getAndIncrement();
		entity.setEntityId(id);
		this.entities.put(id, entity);

		if (entity instanceof PrismarinePlayer player) {
			this.onlinePlayers.put(player.getUniqueId(), player);
		}

		return id;
	}

	/**
	 * Frees an entity from the entity list
	 * @param entity The entity to remove from the list of registered entities
	 */
	public void free(PrismarineEntity entity) {
		this.entities.remove(entity.getEntityId());

		if (entity instanceof PrismarinePlayer player) {
			this.onlinePlayers.remove(player.getUniqueId());
		}
	}
}
