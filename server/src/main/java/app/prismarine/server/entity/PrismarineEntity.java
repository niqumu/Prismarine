package app.prismarine.server.entity;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class PrismarineEntity implements Entity {

	/**
	 * The unique ID of the entity
	 */
	private int entityId = -1; // Default before allocation by the manager

	/**
	 * The Server the entity belongs to
	 */
	private final Server server;

	/**
	 * The current location of the entity
	 */
	private Location location;

	/**
	 * The number of ticks that the entity has been alive for
	 */
	private int ticksLived;

	/**
	 * The number of ticks that the entity is on fire for
	 */
	private int fireTicks;

	/**
	 * Whether the entity is currently on the ground
	 */
	private boolean onGround;

	/**
	 * The distance the entity has fallen since they were last on the ground
	 */
	private float fallDistance;

	/**
	 * Create a new PrismarineEntity given the entity's server and location
	 * @param server The server the entity belongs to
	 * @param location The location of the entity
	 */
	public PrismarineEntity(@NotNull Server server, @NotNull Location location) {
		this.server = server;
		this.location = location;
	}

	/**
	 * Ticks the entity
	 */
	public void tick() {
		this.ticksLived++;

		if (this.fireTicks > 0) {
			this.fireTicks--;
		}
	}

	/**
	 * Gets the entity's current position
	 *
	 * @return a new copy of Location containing the position of this entity
	 */
	@Override @NotNull
	public Location getLocation() {
		return this.location.clone();
	}

	/**
	 * Stores the entity's current position in the provided Location object.
	 * <p>
	 * If the provided Location is null this method does nothing and returns
	 * null.
	 *
	 * @param loc the location to copy into
	 * @return The Location object provided or null
	 */
	@Override @Nullable
	public Location getLocation(@Nullable Location loc) {

		if (loc == null) {
			return null;
		}

		loc.setWorld(this.location.getWorld());
		loc.setX(this.location.getX());
		loc.setY(this.location.getY());
		loc.setZ(this.location.getZ());
		loc.setPitch(this.location.getPitch());
		loc.setYaw(this.location.getYaw());
		return loc;
	}

	/**
	 * Sets this entity's velocity in meters per tick
	 *
	 * @param velocity New velocity to travel with
	 */
	@Override
	public void setVelocity(@NotNull Vector velocity) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets this entity's current velocity
	 *
	 * @return Current traveling velocity of this entity
	 */
	@Override @NotNull
	public Vector getVelocity() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the entity's height
	 *
	 * @return height of entity
	 */
	@Override
	public double getHeight() {
		return this.getBoundingBox().getMaxY() - this.getBoundingBox().getMinY();
	}

	/**
	 * Gets the entity's width
	 *
	 * @return width of entity
	 */
	@Override
	public double getWidth() {
		return this.getBoundingBox().getMaxX() - this.getBoundingBox().getMinX();
	}

	/**
	 * Gets the entity's current bounding box.
	 * <p>
	 * The returned bounding box reflects the entity's current location and
	 * size.
	 *
	 * @return the entity's current bounding box
	 */
	@Override @NotNull
	public BoundingBox getBoundingBox() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns true if the entity is supported by a block. This value is a
	 * state updated by the server and is not recalculated unless the entity
	 * moves.
	 *
	 * @return True if entity is on ground.
	 * @see Player#isOnGround()
	 */
	@Override
	public boolean isOnGround() {
		return this.onGround;
	}

	/**
	 * Returns true if the entity is in water.
	 *
	 * @return <code>true</code> if the entity is in water.
	 */
	@Override
	public boolean isInWater() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the current world this entity resides in
	 *
	 * @return World
	 */
	@Override @NotNull
	public World getWorld() {
		return Objects.requireNonNull(this.location.getWorld());
	}

	/**
	 * Sets the entity's rotation.
	 * <p>
	 * Note that if the entity is affected by AI, it may override this rotation.
	 *
	 * @param yaw the yaw
	 * @param pitch the pitch
	 * @throws UnsupportedOperationException if used for players
	 */
	@Override
	public void setRotation(float yaw, float pitch) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Teleports this entity to the given location. If this entity is riding a
	 * vehicle, it will be dismounted prior to teleportation.
	 *
	 * @param location New location to teleport this entity to
	 * @return <code>true</code> if the teleport was successful
	 */
	@Override
	public boolean teleport(@NotNull Location location) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Teleports this entity to the given location. If this entity is riding a
	 * vehicle, it will be dismounted prior to teleportation.
	 *
	 * @param location New location to teleport this entity to
	 * @param cause The cause of this teleportation
	 * @return <code>true</code> if the teleport was successful
	 */
	@Override
	public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause cause) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Teleports this entity to the target Entity. If this entity is riding a
	 * vehicle, it will be dismounted prior to teleportation.
	 *
	 * @param destination Entity to teleport this entity to
	 * @return <code>true</code> if the teleport was successful
	 */
	@Override
	public boolean teleport(@NotNull Entity destination) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Teleports this entity to the target Entity. If this entity is riding a
	 * vehicle, it will be dismounted prior to teleportation.
	 *
	 * @param destination Entity to teleport this entity to
	 * @param cause The cause of this teleportation
	 * @return <code>true</code> if the teleport was successful
	 */
	@Override
	public boolean teleport(@NotNull Entity destination, @NotNull PlayerTeleportEvent.TeleportCause cause) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns a list of entities within a bounding box centered around this
	 * entity
	 *
	 * @param x 1/2 the size of the box along x axis
	 * @param y 1/2 the size of the box along y axis
	 * @param z 1/2 the size of the box along z axis
	 * @return {@code List<Entity>} List of entities nearby
	 */
	@Override @NotNull
	public List<Entity> getNearbyEntities(double x, double y, double z) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns a unique id for this entity
	 *
	 * @return Entity id
	 */
	@Override
	public int getEntityId() {
		return this.entityId;
	}

	protected void setEntityId(int id) {
		this.entityId = id;
	}

	/**
	 * Returns the entity's current fire ticks (ticks before the entity stops
	 * being on fire).
	 *
	 * @return int fireTicks
	 */
	@Override
	public int getFireTicks() {
		return this.fireTicks;
	}

	/**
	 * Returns the entity's maximum fire ticks.
	 *
	 * @return int maxFireTicks
	 */
	@Override
	public int getMaxFireTicks() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the entity's current fire ticks (ticks before the entity stops
	 * being on fire).
	 *
	 * @param ticks Current ticks remaining
	 */
	@Override
	public void setFireTicks(int ticks) {
		this.fireTicks = ticks;
	}

	/**
	 * Sets if the entity has visual fire (it will always appear to be on fire).
	 *
	 * @param fire whether visual fire is enabled
	 */
	@Override
	public void setVisualFire(boolean fire) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets if the entity has visual fire (it will always appear to be on fire).
	 *
	 * @return whether visual fire is enabled
	 */
	@Override
	public boolean isVisualFire() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the entity's current freeze ticks (amount of ticks the entity has
	 * been in powdered snow).
	 *
	 * @return int freeze ticks
	 */
	@Override
	public int getFreezeTicks() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the entity's maximum freeze ticks (amount of ticks before it will
	 * be fully frozen)
	 *
	 * @return int max freeze ticks
	 */
	@Override
	public int getMaxFreezeTicks() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the entity's current freeze ticks (amount of ticks the entity has
	 * been in powdered snow).
	 *
	 * @param ticks Current ticks
	 */
	@Override
	public void setFreezeTicks(int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets if the entity is fully frozen (it has been in powdered snow for max
	 * freeze ticks).
	 *
	 * @return freeze status
	 */
	@Override
	public boolean isFrozen() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Mark the entity's removal.
	 *
	 * @throws UnsupportedOperationException if you try to remove a {@link Player} use {@link Player#kickPlayer(String)} in this case instead
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns true if this entity has been marked for removal.
	 *
	 * @return True if it is dead.
	 */
	@Override
	public boolean isDead() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns false if the entity has died, been despawned for some other
	 * reason, or has not been added to the world.
	 *
	 * @return True if valid.
	 */
	@Override
	public boolean isValid() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sends this sender a message
	 *
	 * @param message Message to be displayed
	 */
	@Override
	public void sendMessage(@NotNull String message) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sends this sender multiple messages
	 *
	 * @param messages An array of messages to be displayed
	 */
	@Override
	public void sendMessage(@NotNull String... messages) {
		throw new UnsupportedOperationException("Not yet implemented");
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
	 * Gets the {@link Server} that contains this Entity
	 *
	 * @return Server instance running this Entity
	 */
	@Override @NotNull
	public Server getServer() {
		return this.server;
	}

	/**
	 * Gets the name of this command entity
	 *
	 * @return Name of the entity
	 */
	@Override @NotNull
	public String getName() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns true if the entity gets persisted.
	 * <p>
	 * By default all entities are persistent. An entity will also not get
	 * persisted, if it is riding an entity that is not persistent.
	 * <p>
	 * The persistent flag on players controls whether or not to save their
	 * playerdata file when they quit. If a player is directly or indirectly
	 * riding a non-persistent entity, the vehicle at the root and all its
	 * passengers won't get persisted.
	 * <p>
	 * <b>This should not be confused with
	 * {@link LivingEntity#setRemoveWhenFarAway(boolean)} which controls
	 * despawning of living entities. </b>
	 *
	 * @return true if this entity is persistent
	 */
	@Override
	public boolean isPersistent() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether or not the entity gets persisted.
	 *
	 * @param persistent the persistence status
	 * @see #isPersistent()
	 */
	@Override
	public void setPersistent(boolean persistent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the primary passenger of a vehicle. For vehicles that could have
	 * multiple passengers, this will only return the primary passenger.
	 *
	 * @return an entity
	 * @deprecated entities may have multiple passengers, use
	 * {@link #getPassengers()}
	 */
	@Override @Deprecated @Nullable
	public Entity getPassenger() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Set the passenger of a vehicle.
	 *
	 * @param passenger The new passenger.
	 * @return false if it could not be done for whatever reason
	 * @deprecated entities may have multiple passengers, use
	 * {@link #addPassenger(org.bukkit.entity.Entity)}
	 */
	@Override @Deprecated
	public boolean setPassenger(@NotNull Entity passenger) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a list of passengers of this vehicle.
	 * <p>
	 * The returned list will not be directly linked to the entity's current
	 * passengers, and no guarantees are made as to its mutability.
	 *
	 * @return list of entities corresponding to current passengers.
	 */
	@Override @NotNull
	public List<Entity> getPassengers() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Add a passenger to the vehicle.
	 *
	 * @param passenger The passenger to add
	 * @return false if it could not be done for whatever reason
	 */
	@Override
	public boolean addPassenger(@NotNull Entity passenger) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Remove a passenger from the vehicle.
	 *
	 * @param passenger The passenger to remove
	 * @return false if it could not be done for whatever reason
	 */
	@Override
	public boolean removePassenger(@NotNull Entity passenger) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Check if a vehicle has passengers.
	 *
	 * @return True if the vehicle has no passengers.
	 */
	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Eject any passenger.
	 *
	 * @return True if there was a passenger.
	 */
	@Override
	public boolean eject() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the distance this entity has fallen
	 *
	 * @return The distance.
	 */
	@Override
	public float getFallDistance() {
		return this.fallDistance;
	}

	/**
	 * Sets the fall distance for this entity
	 *
	 * @param distance The new distance.
	 */
	@Override
	public void setFallDistance(float distance) {
		this.fallDistance = distance;
	}

	/**
	 * Record the last {@link EntityDamageEvent} inflicted on this entity
	 *
	 * @param event a {@link EntityDamageEvent}
	 * @deprecated method is for internal use only and will be removed
	 */
	@Override @Deprecated(forRemoval = true)
	public void setLastDamageCause(@Nullable EntityDamageEvent event) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Retrieve the last {@link EntityDamageEvent} inflicted on this entity.
	 * This event may have been cancelled.
	 *
	 * @return the last known {@link EntityDamageEvent} or null if hitherto
	 *     unharmed
	 */
	@Override @Nullable
	public EntityDamageEvent getLastDamageCause() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns a unique and persistent id for this entity
	 *
	 * @return unique id
	 */
	@Override @NotNull
	public UUID getUniqueId() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the amount of ticks this entity has lived for.
	 * <p>
	 * This is the equivalent to "age" in entities.
	 *
	 * @return Age of entity
	 */
	@Override
	public int getTicksLived() {
		return this.ticksLived;
	}

	/**
	 * Sets the amount of ticks this entity has lived for.
	 * <p>
	 * This is the equivalent to "age" in entities. May not be less than one
	 * tick.
	 *
	 * @param value Age of entity
	 */
	@Override
	public void setTicksLived(int value) {
		this.ticksLived = value;
	}

	/**
	 * Performs the specified {@link EntityEffect} for this entity.
	 * <p>
	 * This will be viewable to all players near the entity.
	 * <p>
	 * If the effect is not applicable to this class of entity, it will not play.
	 *
	 * @param type Effect to play.
	 */
	@Override
	public void playEffect(@NotNull EntityEffect type) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the type of the entity.
	 *
	 * @return The entity type.
	 */
	@Override @NotNull
	public EntityType getType() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Sound} this entity makes while swimming.
	 *
	 * @return the swimming sound
	 */
	@Override @NotNull
	public Sound getSwimSound() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Sound} this entity makes when splashing in water. For most
	 * entities, this is just {@link Sound#ENTITY_GENERIC_SPLASH}.
	 *
	 * @return the splash sound
	 */
	@Override @NotNull
	public Sound getSwimSplashSound() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Sound} this entity makes when splashing in water at high
	 * speeds. For most entities, this is just {@link Sound#ENTITY_GENERIC_SPLASH}.
	 *
	 * @return the splash sound
	 */
	@Override @NotNull
	public Sound getSwimHighSpeedSplashSound() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns whether this entity is inside a vehicle.
	 *
	 * @return True if the entity is in a vehicle.
	 */
	@Override
	public boolean isInsideVehicle() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Leave the current vehicle. If the entity is currently in a vehicle (and
	 * is removed from it), true will be returned, otherwise false will be
	 * returned.
	 *
	 * @return True if the entity was in a vehicle.
	 */
	@Override
	public boolean leaveVehicle() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the vehicle that this entity is inside. If there is no vehicle,
	 * null will be returned.
	 *
	 * @return The current vehicle.
	 */
	@Override @Nullable
	public Entity getVehicle() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether or not to display the mob's custom name client side. The
	 * name will be displayed above the mob similarly to a player.
	 * <p>
	 * This value has no effect on players, they will always display their
	 * name.
	 *
	 * @param flag custom name or not
	 */
	@Override
	public void setCustomNameVisible(boolean flag) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether or not the mob's custom name is displayed client side.
	 * <p>
	 * This value has no effect on players, they will always display their
	 * name.
	 *
	 * @return if the custom name is displayed
	 */
	@Override
	public boolean isCustomNameVisible() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether or not this entity is visible by default.
	 *
	 * If this entity is not visible by default, then
	 * {@link Player#showEntity(org.bukkit.plugin.Plugin, org.bukkit.entity.Entity)}
	 * will need to be called before the entity is visible to a given player.
	 *
	 * @param visible default visibility status
	 */
	@Override
	public void setVisibleByDefault(boolean visible) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether or not this entity is visible by default.
	 *
	 * If this entity is not visible by default, then
	 * {@link Player#showEntity(org.bukkit.plugin.Plugin, org.bukkit.entity.Entity)}
	 * will need to be called before the entity is visible to a given player.
	 *
	 * @return default visibility status
	 */
	@Override
	public boolean isVisibleByDefault() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get all players that are currently tracking this entity.
	 * <p>
	 * 'Tracking' means that this entity has been sent to the player and that
	 * they are receiving updates on its state. Note that the client's {@code
	 * 'Entity Distance'} setting does not affect the range at which entities
	 * are tracked.
	 *
	 * @return the players tracking this entity, or an empty set if none
	 */
	@Override @NotNull
	public Set<Player> getTrackedBy() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether the entity has a team colored (default: white) glow.
	 *
	 * <b>nb: this refers to the 'Glowing' entity property, not whether a
	 * glowing potion effect is applied</b>
	 *
	 * @param flag if the entity is glowing
	 */
	@Override
	public void setGlowing(boolean flag) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the entity is glowing or not.
	 *
	 * <b>nb: this refers to the 'Glowing' entity property, not whether a
	 * glowing potion effect is applied</b>
	 *
	 * @return whether the entity is glowing
	 */
	@Override
	public boolean isGlowing() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether the entity is invulnerable or not.
	 * <p>
	 * When an entity is invulnerable it can only be damaged by players in
	 * creative mode.
	 *
	 * @param flag if the entity is invulnerable
	 */
	@Override
	public void setInvulnerable(boolean flag) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the entity is invulnerable or not.
	 *
	 * @return whether the entity is
	 */
	@Override
	public boolean isInvulnerable() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the entity is silent or not.
	 *
	 * @return whether the entity is silent.
	 */
	@Override
	public boolean isSilent() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether the entity is silent or not.
	 * <p>
	 * When an entity is silent it will not produce any sound.
	 *
	 * @param flag if the entity is silent
	 */
	@Override
	public void setSilent(boolean flag) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns whether gravity applies to this entity.
	 *
	 * @return whether gravity applies
	 */
	@Override
	public boolean hasGravity() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether gravity applies to this entity.
	 *
	 * @param gravity whether gravity should apply
	 */
	@Override
	public void setGravity(boolean gravity) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the period of time (in ticks) before this entity can use a portal.
	 *
	 * @return portal cooldown ticks
	 */
	@Override
	public int getPortalCooldown() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the period of time (in ticks) before this entity can use a portal.
	 *
	 * @param cooldown portal cooldown ticks
	 */
	@Override
	public void setPortalCooldown(int cooldown) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns a set of tags for this entity.
	 * <br>
	 * Entities can have no more than 1024 tags.
	 *
	 * @return a set of tags for this entity
	 */
	@Override @NotNull
	public Set<String> getScoreboardTags() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Add a tag to this entity.
	 * <br>
	 * Entities can have no more than 1024 tags.
	 *
	 * @param tag the tag to add
	 * @return true if the tag was successfully added
	 */
	@Override
	public boolean addScoreboardTag(@NotNull String tag) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Removes a given tag from this entity.
	 *
	 * @param tag the tag to remove
	 * @return true if the tag was successfully removed
	 */
	@Override
	public boolean removeScoreboardTag(@NotNull String tag) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the reaction of the entity when moved by a piston.
	 *
	 * @return reaction
	 */
	@Override @NotNull
	public PistonMoveReaction getPistonMoveReaction() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the closest cardinal {@link BlockFace} direction an entity is
	 * currently facing.
	 * <br>
	 * This will not return any non-cardinal directions such as
	 * {@link BlockFace#UP} or {@link BlockFace#DOWN}.
	 * <br>
	 * {@link Hanging} entities will override this call and thus their behavior
	 * may be different.
	 *
	 * @return the entity's current cardinal facing.
	 * @see Hanging
	 * @see Directional#getFacing()
	 */
	@Override @NotNull
	public BlockFace getFacing() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the entity's current pose.
	 *
	 * <b>Note that the pose is only updated at the end of a tick, so may be
	 * inconsistent with other methods. eg {@link Player#isSneaking()} being
	 * true does not imply the current pose will be {@link Pose#SNEAKING}</b>
	 *
	 * @return current pose
	 */
	@Override @NotNull
	public Pose getPose() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the category of spawn to which this entity belongs.
	 *
	 * @return the entity´s category spawn
	 */
	@Override @NotNull
	public SpawnCategory getSpawnCategory() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks if this entity has been spawned in a world. <br>
	 * Entities not spawned in a world will not tick, be sent to players, or be
	 * saved to the server files.
	 *
	 * @return whether the entity has been spawned in a world
	 */
	@Override
	public boolean isInWorld() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get this entity as an NBT string.
	 * <p>
	 * This string should not be relied upon as a serializable value.
	 *
	 * @return the NBT string or null if one cannot be made
	 */
	@Override @Nullable
	public String getAsString() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Crates an {@link EntitySnapshot} representing the current state of this entity.
	 *
	 * @return a snapshot representing this entity or null if one cannot be made
	 */
	@Override @Nullable
	public EntitySnapshot createSnapshot() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates a copy of this entity and all its data. Does not spawn the copy in
	 * the world. <br>
	 * <b>Note:</b> Players cannot be copied.
	 *
	 * @return a copy of this entity.
	 */
	@Override @NotNull
	public Entity copy() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates a copy of this entity and all its data. Spawns the copy at the given location. <br>
	 * <b>Note:</b> Players cannot be copied.
	 * @param to the location to copy to
	 * @return a copy of this entity.
	 */
	@Override @NotNull
	public Entity copy(@NotNull Location to) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * @return Additional Spigot APIs
	 */
	@Override @NotNull
	public Entity.Spigot spigot() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the custom name on a mob or block. If there is no name this method
	 * will return null.
	 * <p>
	 * This value has no effect on players, they will always use their real
	 * name.
	 *
	 * @return name of the mob/block or null
	 */
	@Override @Nullable
	public String getCustomName() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets a custom name on a mob or block. This name will be used in death
	 * messages and can be sent to the client as a nameplate over the mob.
	 * <p>
	 * Setting the name to null or an empty string will clear it.
	 * <p>
	 * This value has no effect on players, they will always use their real
	 * name.
	 *
	 * @param name the name to set
	 */
	@Override
	public void setCustomName(String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets a metadata value in the implementing object's metadata store.
	 *
	 * @param metadataKey A unique key to identify this metadata.
	 * @param newMetadataValue The metadata value to apply.
	 * @throws IllegalArgumentException If value is null, or the owning plugin
	 *     is null
	 */
	@Override
	public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns a list of previously set metadata values from the implementing
	 * object's metadata store.
	 *
	 * @param metadataKey the unique metadata key being sought.
	 * @return A list of values, one for each plugin that has set the
	 *     requested value.
	 */
	@Override @NotNull
	public List<MetadataValue> getMetadata(@NotNull String metadataKey) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Tests to see whether the implementing object contains the given
	 * metadata value in its metadata store.
	 *
	 * @param metadataKey the unique metadata key being queried.
	 * @return the existence of the metadataKey within subject.
	 */
	@Override
	public boolean hasMetadata(@NotNull String metadataKey) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Removes the given metadata value from the implementing object's
	 * metadata store.
	 *
	 * @param metadataKey the unique metadata key identifying the metadata to
	 *     remove.
	 * @param owningPlugin This plugin's metadata value will be removed. All
	 *     other values will be left untouched.
	 * @throws IllegalArgumentException If plugin is null
	 */
	@Override
	public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks if this object contains an override for the specified
	 * permission, by fully qualified name
	 *
	 * @param name Name of the permission
	 * @return true if the permission is set, otherwise false
	 */
	@Override
	public boolean isPermissionSet(@NotNull String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks if this object contains an override for the specified {@link
	 * Permission}
	 *
	 * @param perm Permission to check
	 * @return true if the permission is set, otherwise false
	 */
	@Override
	public boolean isPermissionSet(@NotNull Permission perm) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the value of the specified permission, if set.
	 * <p>
	 * If a permission override is not set on this object, the default value
	 * of the permission will be returned.
	 *
	 * @param name Name of the permission
	 * @return Value of the permission
	 */
	@Override
	public boolean hasPermission(@NotNull String name) {
		return true; // TODO: everyone can do everything right now!
	}

	/**
	 * Gets the value of the specified permission, if set.
	 * <p>
	 * If a permission override is not set on this object, the default value
	 * of the permission will be returned
	 *
	 * @param perm Permission to get
	 * @return Value of the permission
	 */
	@Override
	public boolean hasPermission(@NotNull Permission perm) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds a new {@link PermissionAttachment} with a single permission by
	 * name and value
	 *
	 * @param plugin Plugin responsible for this attachment, may not be null
	 *     or disabled
	 * @param name Name of the permission to attach
	 * @param value Value of the permission
	 * @return The PermissionAttachment that was just created
	 */
	@Override @NotNull
	public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds a new empty {@link PermissionAttachment} to this object
	 *
	 * @param plugin Plugin responsible for this attachment, may not be null
	 *     or disabled
	 * @return The PermissionAttachment that was just created
	 */
	@Override @NotNull
	public PermissionAttachment addAttachment(@NotNull Plugin plugin) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Temporarily adds a new {@link PermissionAttachment} with a single
	 * permission by name and value
	 *
	 * @param plugin Plugin responsible for this attachment, may not be null
	 *     or disabled
	 * @param name Name of the permission to attach
	 * @param value Value of the permission
	 * @param ticks Amount of ticks to automatically remove this attachment
	 *     after
	 * @return The PermissionAttachment that was just created
	 */
	@Override @Nullable
	public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name,
	                                          boolean value, int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Temporarily adds a new empty {@link PermissionAttachment} to this
	 * object
	 *
	 * @param plugin Plugin responsible for this attachment, may not be null
	 *     or disabled
	 * @param ticks Amount of ticks to automatically remove this attachment
	 *     after
	 * @return The PermissionAttachment that was just created
	 */
	@Override @Nullable
	public PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Removes the given {@link PermissionAttachment} from this object
	 *
	 * @param attachment Attachment to remove
	 * @throws IllegalArgumentException Thrown when the specified attachment
	 *     isn't part of this object
	 */
	@Override
	public void removeAttachment(@NotNull PermissionAttachment attachment) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Recalculates the permissions for this object, if the attachments have
	 * changed values.
	 * <p>
	 * This should very rarely need to be called from a plugin.
	 */
	@Override
	public void recalculatePermissions() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a set containing all of the permissions currently in effect by
	 * this object
	 *
	 * @return Set of currently effective permissions
	 */
	@Override @NotNull
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
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

	/**
	 * Returns a custom tag container capable of storing tags on the object.
	 *
	 * Note that the tags stored on this container are all stored under their
	 * own custom namespace therefore modifying default tags using this
	 * {@link PersistentDataHolder} is impossible.
	 *
	 * @return the persistent metadata container
	 */
	@Override @NotNull
	public PersistentDataContainer getPersistentDataContainer() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
