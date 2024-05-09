package app.prismarine.api.entity;

import app.prismarine.api.Location;
import app.prismarine.api.Server;

/**
 * Represents an entity in a world
 */
public interface Entity {

	/**
	 * Gets the unique ID of this entity
	 * @return The entity's ID
	 */
	int getEntityId();

	/**
	 * Gets the current {@link Location} of this entity
	 * @return The location of this entity
	 */
	Location getLocation();

	/**
	 * Whether this entity is currently on the ground
	 * @return The entity's current ground state
	 */
	boolean isOnGround();

	/**
	 * Gets the distance the entity has fallen since they were last on the ground
	 * @return The distance the entity has fallen
	 */
	float getFallDistance();

	/**
	 * Sets the entity's current fall distance
	 * @param fallDistance The new fall distance to use
	 */
	void setFallDistance(float fallDistance);

	/**
	 * Teleports the entity to the specified location
	 * @param newLocation The location to teleprot the entity to
	 */
	void teleport(Location newLocation);

	/**
	 * Gets the {@link Server} the entity belongs to
	 * @return The entity's server
	 */
	Server getServer();
}
