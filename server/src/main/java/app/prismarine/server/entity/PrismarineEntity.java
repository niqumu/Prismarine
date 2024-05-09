package app.prismarine.server.entity;

import app.prismarine.api.Location;
import app.prismarine.api.Server;
import app.prismarine.api.entity.Entity;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class PrismarineEntity implements Entity {

	/**
	 * The unique ID of the entity
	 */
	@Getter
	private final int entityId;

	/**
	 * The Server the entity belongs to
	 */
	@Getter
	private final Server server;

	/**
	 * The current location of the entity
	 */
	@Getter
	private Location location;

	/**
	 * Whether the entity is currently on the ground
	 */
	@Getter
	private boolean onGround;

	/**
	 * The distance the entity has fallen since they were last on the ground
	 */
	@Getter @Setter
	private float fallDistance;

	public PrismarineEntity(@NonNull Server server, @NonNull Location location) {
		this.server = server;
		this.location = location;
		this.entityId = 0; // todo
	}

	/**
	 * Teleports the entity to the specified location
	 *
	 * @param newLocation The location to teleprot the entity to
	 */
	@Override
	public void teleport(Location newLocation) {
		this.location = newLocation; // todo
	}
}
