package app.prismarine.server.entity;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityManager {

	/**
	 * A map of all entities, keyed by their entity ID
	 */
	@Getter
	private final Map<Integer, PrismarineEntity> entities = new ConcurrentHashMap<>();

	private AtomicInteger lastID = new AtomicInteger(1);

	public void tick() {
		this.entities.values().forEach(PrismarineEntity::tick);
	}

	/**
	 * Gets an entity by its entity ID
	 * @param id The ID of the entity to retrieve
	 * @return The entity with the provided ID, or {@code null} if no such entity exists
	 */
	public PrismarineEntity getEntity(int id) {
		return this.entities.get(id);
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
		return id;
	}

	/**
	 * Frees an entity from the entity list
	 * @param entity The entity to remove from the list of registered entities
	 */
	private void free(PrismarineEntity entity) {
		this.entities.remove(entity.getEntityId());
	}
}
