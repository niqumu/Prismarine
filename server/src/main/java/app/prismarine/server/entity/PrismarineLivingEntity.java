package app.prismarine.server.entity;

import lombok.NonNull;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PrismarineLivingEntity extends PrismarineEntity implements LivingEntity {

	/**
	 * The entity's health from 0 to {@link #getMaxHealth()}, where 0 is dead
	 */
	protected double health;

	/**
	 * The maximum health this entity has
	 */
	protected double maxHealth;
	private final double originalMaxHealth;

	private int noDamageTicks;


	public PrismarineLivingEntity(@NotNull Server server, @NotNull Location location, int maxHealth) {
		super(server, location);

		this.maxHealth = maxHealth;
		this.health = this.originalMaxHealth = this.maxHealth;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.noDamageTicks > 0) {
			this.noDamageTicks--;
		}
	}

	/**
	 * Gets the height of the living entity's eyes above its Location.
	 *
	 * @return height of the living entity's eyes above its location
	 */
	@Override
	public double getEyeHeight() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the height of the living entity's eyes above its Location.
	 *
	 * @param ignorePose if set to true, the effects of pose changes, eg
	 *     sneaking and gliding will be ignored
	 * @return height of the living entity's eyes above its location
	 */
	@Override
	public double getEyeHeight(boolean ignorePose) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get a Location detailing the current eye position of the living entity.
	 *
	 * @return a location at the eyes of the living entity
	 */
	@Override @NotNull
	public Location getEyeLocation() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets all blocks along the living entity's line of sight.
	 * <p>
	 * This list contains all blocks from the living entity's eye position to
	 * target inclusive. This method considers all blocks as 1x1x1 in size.
	 *
	 * @param transparent Set containing all transparent block Materials (set to
	 *     null for only air)
	 * @param maxDistance this is the maximum distance to scan (may be limited
	 *     by server by at least 100 blocks, no less)
	 * @return list containing all blocks along the living entity's line of
	 *     sight
	 */
	@Override @NotNull
	public List<Block> getLineOfSight(@Nullable Set<Material> transparent, int maxDistance) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the block that the living entity has targeted.
	 * <p>
	 * This method considers all blocks as 1x1x1 in size. To take exact block
	 * collision shapes into account, see {@link #getTargetBlockExact(int,
	 * FluidCollisionMode)}.
	 *
	 * @param transparent Set containing all transparent block Materials (set to
	 *     null for only air)
	 * @param maxDistance this is the maximum distance to scan (may be limited
	 *     by server by at least 100 blocks, no less)
	 * @return block that the living entity has targeted
	 */
	@Override @NotNull
	public Block getTargetBlock(@Nullable Set<Material> transparent, int maxDistance) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the last two blocks along the living entity's line of sight.
	 * <p>
	 * The target block will be the last block in the list. This method
	 * considers all blocks as 1x1x1 in size.
	 *
	 * @param transparent Set containing all transparent block Materials (set to
	 *     null for only air)
	 * @param maxDistance this is the maximum distance to scan. This may be
	 *     further limited by the server, but never to less than 100 blocks
	 * @return list containing the last 2 blocks along the living entity's
	 *     line of sight
	 */
	@Override @NotNull
	public List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> transparent, int maxDistance) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the block that the living entity has targeted.
	 * <p>
	 * This takes the blocks' precise collision shapes into account. Fluids are
	 * ignored.
	 * <p>
	 * This may cause loading of chunks! Some implementations may impose
	 * artificial restrictions on the maximum distance.
	 *
	 * @param maxDistance the maximum distance to scan
	 * @return block that the living entity has targeted
	 * @see #getTargetBlockExact(int, org.bukkit.FluidCollisionMode)
	 */
	@Override @Nullable
	public Block getTargetBlockExact(int maxDistance) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the block that the living entity has targeted.
	 * <p>
	 * This takes the blocks' precise collision shapes into account.
	 * <p>
	 * This may cause loading of chunks! Some implementations may impose
	 * artificial restrictions on the maximum distance.
	 *
	 * @param maxDistance the maximum distance to scan
	 * @param fluidCollisionMode the fluid collision mode
	 * @return block that the living entity has targeted
	 * @see #rayTraceBlocks(double, FluidCollisionMode)
	 */
	@Override @Nullable
	public Block getTargetBlockExact(int maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Performs a ray trace that provides information on the targeted block.
	 * <p>
	 * This takes the blocks' precise collision shapes into account. Fluids are
	 * ignored.
	 * <p>
	 * This may cause loading of chunks! Some implementations may impose
	 * artificial restrictions on the maximum distance.
	 *
	 * @param maxDistance the maximum distance to scan
	 * @return information on the targeted block, or <code>null</code> if there
	 *     is no targeted block in range
	 * @see #rayTraceBlocks(double, FluidCollisionMode)
	 */
	@Override @Nullable
	public RayTraceResult rayTraceBlocks(double maxDistance) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Performs a ray trace that provides information on the targeted block.
	 * <p>
	 * This takes the blocks' precise collision shapes into account.
	 * <p>
	 * This may cause loading of chunks! Some implementations may impose
	 * artificial restrictions on the maximum distance.
	 *
	 * @param maxDistance the maximum distance to scan
	 * @param fluidCollisionMode the fluid collision mode
	 * @return information on the targeted block, or <code>null</code> if there
	 *     is no targeted block in range
	 * @see World#rayTraceBlocks(Location, Vector, double, FluidCollisionMode)
	 */
	@Override @Nullable
	public RayTraceResult rayTraceBlocks(double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the amount of air that the living entity has remaining, in
	 * ticks.
	 *
	 * @return amount of air remaining
	 */
	@Override
	public int getRemainingAir() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the amount of air that the living entity has remaining, in ticks.
	 *
	 * @param ticks amount of air remaining
	 */
	@Override
	public void setRemainingAir(int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the maximum amount of air the living entity can have, in ticks.
	 *
	 * @return maximum amount of air
	 */
	@Override
	public int getMaximumAir() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the maximum amount of air the living entity can have, in ticks.
	 *
	 * @param ticks maximum amount of air
	 */
	@Override
	public void setMaximumAir(int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the item that the player is using (eating food, drawing back a bow,
	 * blocking, etc.)
	 *
	 * @return the item being used by the player, or null if they are not using
	 * an item
	 */
	@Override @Nullable
	public ItemStack getItemInUse() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the number of ticks remaining for the current item's usage.
	 *
	 * @return The number of ticks remaining
	 */
	@Override
	public int getItemInUseTicks() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the number of ticks that remain for the current item's usage.
	 * Applies to items that take time to use, like eating food, drawing a bow,
	 * or throwing a trident.
	 *
	 * @param ticks The number of ticks remaining
	 */
	@Override
	public void setItemInUseTicks(int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the time in ticks until the next arrow leaves the entity's body.
	 *
	 * @return ticks until arrow leaves
	 */
	@Override
	public int getArrowCooldown() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the time in ticks until the next arrow leaves the entity's body.
	 *
	 * @param ticks time until arrow leaves
	 */
	@Override
	public void setArrowCooldown(int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the amount of arrows in an entity's body.
	 *
	 * @return amount of arrows in body
	 */
	@Override
	public int getArrowsInBody() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Set the amount of arrows in the entity's body.
	 *
	 * @param count amount of arrows in entity's body
	 */
	@Override
	public void setArrowsInBody(int count) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the living entity's current maximum no damage ticks.
	 * <p>
	 * This is the maximum duration in which the living entity will not take
	 * damage.
	 *
	 * @return maximum no damage ticks
	 */
	@Override
	public int getMaximumNoDamageTicks() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the living entity's current maximum no damage ticks.
	 *
	 * @param ticks maximum amount of no damage ticks
	 */
	@Override
	public void setMaximumNoDamageTicks(int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the living entity's last damage taken in the current no damage
	 * ticks time.
	 * <p>
	 * Only damage higher than this amount will further damage the living
	 * entity.
	 *
	 * @return damage taken since the last no damage ticks time period
	 */
	@Override
	public double getLastDamage() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the damage dealt within the current no damage ticks time period.
	 *
	 * @param damage amount of damage
	 */
	@Override
	public void setLastDamage(double damage) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the living entity's current no damage ticks.
	 *
	 * @return amount of no damage ticks
	 */
	@Override
	public int getNoDamageTicks() {
		return this.noDamageTicks;
	}

	/**
	 * Sets the living entity's current no damage ticks.
	 *
	 * @param ticks amount of no damage ticks
	 */
	@Override
	public void setNoDamageTicks(int ticks) {
		this.noDamageTicks = ticks;
	}

	/**
	 * Get the ticks that this entity has performed no action.
	 * <p>
	 * The details of what "no action ticks" entails varies from entity to entity
	 * and cannot be specifically defined. Some examples include squid using this
	 * value to determine when to swim, raiders for when they are to be expelled
	 * from raids, or creatures (such as withers) as a requirement to be despawned.
	 *
	 * @return amount of no action ticks
	 */
	@Override
	public int getNoActionTicks() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Set the ticks that this entity has performed no action.
	 * <p>
	 * The details of what "no action ticks" entails varies from entity to entity
	 * and cannot be specifically defined. Some examples include squid using this
	 * value to determine when to swim, raiders for when they are to be expelled
	 * from raids, or creatures (such as withers) as a requirement to be despawned.
	 *
	 * @param ticks amount of no action ticks
	 */
	@Override
	public void setNoActionTicks(int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player identified as the killer of the living entity.
	 * <p>
	 * May be null.
	 *
	 * @return killer player, or null if none found
	 */
	@Override @Nullable
	public Player getKiller() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds the given {@link PotionEffect} to the living entity.
	 *
	 * @param effect PotionEffect to be added
	 * @return whether the effect could be added
	 */
	@Override
	public boolean addPotionEffect(@NotNull PotionEffect effect) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds the given {@link PotionEffect} to the living entity.
	 * <p>
	 * Only one potion effect can be present for a given {@link
	 * PotionEffectType}.
	 *
	 * @param effect PotionEffect to be added
	 * @param force whether conflicting effects should be removed
	 * @return whether the effect could be added
	 * @deprecated no need to force since multiple effects of the same type are
	 * now supported.
	 */
	@Override @Deprecated
	public boolean addPotionEffect(@NotNull PotionEffect effect, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Attempts to add all of the given {@link PotionEffect} to the living
	 * entity.
	 *
	 * @param effects the effects to add
	 * @return whether all of the effects could be added
	 */
	@Override
	public boolean addPotionEffects(@NotNull Collection<PotionEffect> effects) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns whether the living entity already has an existing effect of
	 * the given {@link PotionEffectType} applied to it.
	 *
	 * @param type the potion type to check
	 * @return whether the living entity has this potion effect active on them
	 */
	@Override
	public boolean hasPotionEffect(@NotNull PotionEffectType type) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the active {@link PotionEffect} of the specified type.
	 * <p>
	 * If the effect is not present on the entity then null will be returned.
	 *
	 * @param type the potion type to check
	 * @return the effect active on this entity, or null if not active.
	 */
	@Override @NotNull
	public PotionEffect getPotionEffect(@Nullable PotionEffectType type) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Removes any effects present of the given {@link PotionEffectType}.
	 *
	 * @param type the potion type to remove
	 */
	@Override
	public void removePotionEffect(@NotNull PotionEffectType type) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns all currently active {@link PotionEffect}s on the living
	 * entity.
	 *
	 * @return a collection of {@link PotionEffect}s
	 */
	@Override @NotNull
	public Collection<PotionEffect> getActivePotionEffects() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks whether the living entity has block line of sight to another.
	 * <p>
	 * This uses the same algorithm that hostile mobs use to find the closest
	 * player.
	 *
	 * @param other the entity to determine line of sight to
	 * @return true if there is a line of sight, false if not
	 */
	@Override
	public boolean hasLineOfSight(@NotNull Entity other) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns if the living entity despawns when away from players or not.
	 * <p>
	 * By default, animals are not removed while other mobs are.
	 *
	 * @return true if the living entity is removed when away from players
	 */
	@Override
	public boolean getRemoveWhenFarAway() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether or not the living entity despawns when away from players
	 * or not.
	 *
	 * @param remove the removal status
	 */
	@Override
	public void setRemoveWhenFarAway(boolean remove) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the inventory with the equipment worn by the living entity.
	 *
	 * @return the living entity's inventory
	 */
	@Override @Nullable
	public EntityEquipment getEquipment() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether or not the living entity can pick up items.
	 *
	 * @param pickup whether or not the living entity can pick up items
	 */
	@Override
	public void setCanPickupItems(boolean pickup) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets if the living entity can pick up items.
	 *
	 * @return whether or not the living entity can pick up items
	 */
	@Override
	public boolean getCanPickupItems() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns whether the entity is currently leashed.
	 *
	 * @return whether the entity is leashed
	 */
	@Override
	public boolean isLeashed() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the entity that is currently leading this entity.
	 *
	 * @return the entity holding the leash
	 * @throws IllegalStateException if not currently leashed
	 */
	@Override @NotNull
	public Entity getLeashHolder() throws IllegalStateException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the leash on this entity to be held by the supplied entity.
	 * <p>
	 * This method has no effect on EnderDragons, Withers, Players, or Bats.
	 * Non-living entities excluding leashes will not persist as leash
	 * holders.
	 *
	 * @param holder the entity to leash this entity to, or null to unleash
	 * @return whether the operation was successful
	 */
	@Override
	public boolean setLeashHolder(@Nullable Entity holder) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks to see if an entity is gliding, such as using an Elytra.
	 * @return True if this entity is gliding.
	 */
	@Override
	public boolean isGliding() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Makes entity start or stop gliding. This will work even if an Elytra
	 * is not equipped, but will be reverted by the server immediately after
	 * unless an event-cancelling mechanism is put in place.
	 * @param gliding True if the entity is gliding.
	 */
	@Override
	public void setGliding(boolean gliding) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks to see if an entity is swimming.
	 *
	 * @return True if this entity is swimming.
	 */
	@Override
	public boolean isSwimming() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Makes entity start or stop swimming.
	 *
	 * This may have unexpected results if the entity is not in water.
	 *
	 * @param swimming True if the entity is swimming.
	 */
	@Override
	public void setSwimming(boolean swimming) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks to see if an entity is currently using the Riptide enchantment.
	 *
	 * @return True if this entity is currently riptiding.
	 */
	@Override
	public boolean isRiptiding() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns whether this entity is slumbering.
	 *
	 * @return slumber state
	 */
	@Override
	public boolean isSleeping() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets if the entity is climbing.
	 *
	 * @return if the entity is climbing
	 */
	@Override
	public boolean isClimbing() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether an entity will have AI.
	 *
	 * The entity will be completely unable to move if it has no AI.
	 *
	 * @param ai whether the mob will have AI or not.
	 */
	@Override
	public void setAI(boolean ai) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks whether an entity has AI.
	 *
	 * The entity will be completely unable to move if it has no AI.
	 *
	 * @return true if the entity has AI, otherwise false.
	 */
	@Override
	public boolean hasAI() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Makes this entity attack the given entity with a melee attack.
	 *
	 * Attack damage is calculated by the server from the attributes and
	 * equipment of this mob, and knockback is applied to {@code target} as
	 * appropriate.
	 *
	 * @param target entity to attack.
	 */
	@Override
	public void attack(@NotNull Entity target) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Makes this entity swing their main hand.
	 *
	 * This method does nothing if this entity does not have an animation for
	 * swinging their main hand.
	 */
	@Override
	public void swingMainHand() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Makes this entity swing their off hand.
	 *
	 * This method does nothing if this entity does not have an animation for
	 * swinging their off hand.
	 */
	@Override
	public void swingOffHand() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Makes this entity flash red as if they were damaged.
	 *
	 * @param yaw The direction the damage is coming from in relation to the
	 * entity, where 0 is in front of the player, 90 is to the right, 180 is
	 * behind, and 270 is to the left
	 */
	@Override
	public void playHurtAnimation(float yaw) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Set if this entity will be subject to collisions with other entities.
	 * <p>
	 * Exemptions to this rule can be managed with
	 * {@link #getCollidableExemptions()}
	 * <p>
	 * Note that the client may predict the collision between itself and another
	 * entity, resulting in this flag not working for player collisions. This
	 * method should therefore only be used to set the collision status of
	 * non-player entities.
	 * <p>
	 * To control player collisions, use {@link Team.Option#COLLISION_RULE} in
	 * combination with a {@link Scoreboard} and a {@link Team}.
	 *
	 * @param collidable collision status
	 */
	@Override
	public void setCollidable(boolean collidable) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets if this entity is subject to collisions with other entities.
	 * <p>
	 * Some entities might be exempted from the collidable rule of this entity.
	 * Use {@link #getCollidableExemptions()} to get these.
	 * <p>
	 * Please note that this method returns only the custom collidable state,
	 * not whether the entity is non-collidable for other reasons such as being
	 * dead.
	 * <p>
	 * Note that the client may predict the collision between itself and another
	 * entity, resulting in this flag not being accurate for player collisions.
	 * This method should therefore only be used to check the collision status
	 * of non-player entities.
	 * <p>
	 * To check the collision behavior for a player, use
	 * {@link Team.Option#COLLISION_RULE} in combination with a
	 * {@link Scoreboard} and a {@link Team}.
	 *
	 * @return collision status
	 */
	@Override
	public boolean isCollidable() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a mutable set of UUIDs of the entities which are exempt from the
	 * entity's collidable rule and which's collision with this entity will
	 * behave the opposite of it.
	 * <p>
	 * This set can be modified to add or remove exemptions.
	 * <p>
	 * For example if collidable is true and an entity is in the exemptions set
	 * then it will not collide with it. Similarly if collidable is false and an
	 * entity is in this set then it will still collide with it.
	 * <p>
	 * Note these exemptions are not (currently) persistent.
	 * <p>
	 * Note that the client may predict the collision between itself and another
	 * entity, resulting in those exemptions not being accurate for player
	 * collisions. This method should therefore only be used to exempt
	 * non-player entities.
	 * <p>
	 * To exempt collisions for a player, use {@link Team.Option#COLLISION_RULE}
	 * in combination with a {@link Scoreboard} and a {@link Team}.
	 *
	 * @return the collidable exemption set
	 */
	@Override @NotNull
	public Set<UUID> getCollidableExemptions() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the value of the memory specified.
	 * <p>
	 * Note that the value is null when the specific entity does not have that
	 * value by default.
	 *
	 * @param memoryKey memory to access
	 * @param <T> the type of the return value
	 * @return a instance of the memory section value or null if not present
	 */
	@Override @Nullable
	public <T> T getMemory(@NotNull MemoryKey<T> memoryKey) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the value of the memory specified.
	 * <p>
	 * Note that the value will not be persisted when the specific entity does
	 * not have that value by default.
	 *
	 * @param memoryKey the memory to access
	 * @param memoryValue a typed memory value
	 * @param <T> the type of the passed value
	 */
	@Override
	public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T memoryValue) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Sound} this entity will make when damaged.
	 *
	 * @return the hurt sound, or null if the entity does not make any sound
	 */
	@Override @Nullable
	public Sound getHurtSound() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Sound} this entity will make on death.
	 *
	 * @return the death sound, or null if the entity does not make any sound
	 */
	@Override @Nullable
	public Sound getDeathSound() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Sound} this entity will make when falling from the given
	 * height (in blocks). The sound will often differ between either a small
	 * or a big fall damage sound if the height exceeds 4 blocks.
	 *
	 * @param fallHeight the fall height in blocks
	 * @return the fall damage sound
	 * @see #getFallDamageSoundSmall()
	 * @see #getFallDamageSoundBig()
	 */
	@Override @NotNull
	public Sound getFallDamageSound(int fallHeight) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Sound} this entity will make when falling from a small
	 * height.
	 *
	 * @return the fall damage sound
	 */
	@Override @NotNull
	public Sound getFallDamageSoundSmall() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Sound} this entity will make when falling from a large
	 * height.
	 *
	 * @return the fall damage sound
	 */
	@Override @NotNull
	public Sound getFallDamageSoundBig() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Sound} this entity will make when drinking the given
	 * {@link ItemStack}.
	 *
	 * @param itemStack the item stack being drank
	 * @return the drinking sound
	 */
	@Override @NotNull
	public Sound getDrinkingSound(@NotNull ItemStack itemStack) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Sound} this entity will make when eating the given
	 * {@link ItemStack}.
	 *
	 * @param itemStack the item stack being eaten
	 * @return the eating sound
	 */
	@Override @NotNull
	public Sound getEatingSound(@NotNull ItemStack itemStack) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns true if this entity can breathe underwater and will not take
	 * suffocation damage when its air supply reaches zero.
	 *
	 * @return <code>true</code> if the entity can breathe underwater
	 */
	@Override
	public boolean canBreatheUnderwater() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the category to which this entity belongs.
	 *
	 * Categories may subject this entity to additional effects, benefits or
	 * debuffs.
	 *
	 * @return the entity category
	 * @deprecated entity groupings are now managed by tags, not categories
	 */
	@Override @NotNull @Deprecated
	public EntityCategory getCategory() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether the entity is invisible or not.
	 *
	 * @param invisible If the entity is invisible
	 */
	@Override
	public void setInvisible(boolean invisible) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the entity is invisible or not.
	 *
	 * @return Whether the entity is invisible
	 */
	@Override
	public boolean isInvisible() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the specified attribute instance from the object. This instance will
	 * be backed directly to the object and any changes will be visible at once.
	 *
	 * @param attribute the attribute to get
	 * @return the attribute instance or null if not applicable to this object
	 */
	@Override @Nullable
	public AttributeInstance getAttribute(@NotNull Attribute attribute) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Deals the given amount of damage to this entity.
	 *
	 * @param amount Amount of damage to deal
	 */
	@Override
	public void damage(double amount) {
		this.damage(amount, (Entity) null);
	}

	/**
	 * Deals the given amount of damage to this entity from a specified
	 * {@link Entity}.
	 *
	 * @param amount amount of damage to deal
	 * @param source entity to which the damage should be attributed
	 */
	@Override
	public void damage(double amount, @Nullable Entity source) {
		if (this.noDamageTicks > 0 || this.health <= 0) {
			return;
		}

		this.setHealth(this.health - amount);
	}

	/**
	 * Deals the given amount of damage to this entity from a specified
	 * {@link DamageSource}.
	 *
	 * @param amount amount of damage to deal
	 * @param damageSource source to which the damage should be attributed
	 */
	@Override
	public void damage(double amount, @NotNull DamageSource damageSource) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the entity's health from 0 to {@link #getMaxHealth()}, where 0 is dead.
	 *
	 * @return Health represented from 0 to max
	 */
	@Override
	public double getHealth() {
		return this.health;
	}

	/**
	 * Sets the entity's health from 0 to {@link #getMaxHealth()}, where 0 is
	 * dead.
	 *
	 * @param health New health represented from 0 to max
	 * @throws IllegalArgumentException Thrown if the health is {@literal < 0 or >}
	 *     {@link #getMaxHealth()}
	 */
	@Override
	public void setHealth(double health) {
		this.health = health;
	}

	/**
	 * Gets the entity's absorption amount.
	 *
	 * @return absorption amount from 0
	 */
	@Override
	public double getAbsorptionAmount() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the entity's absorption amount.
	 * <p>
	 * Note: The amount is capped to the value of
	 * {@link Attribute#GENERIC_MAX_ABSORPTION}. The effect of this method on
	 * that attribute is currently unspecified and subject to change.
	 *
	 * @param amount new absorption amount from 0
	 * @throws IllegalArgumentException thrown if health is {@literal < 0} or
	 * non-finite.
	 */
	@Override
	public void setAbsorptionAmount(double amount) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the maximum health this entity has.
	 *
	 * @return Maximum health
	 * @deprecated use {@link Attribute#GENERIC_MAX_HEALTH}.
	 */
	@Override @Deprecated
	public double getMaxHealth() {
		return this.maxHealth;
	}

	/**
	 * Sets the maximum health this entity can have.
	 * <p>
	 * If the health of the entity is above the value provided it will be set
	 * to that value.
	 * <p>
	 * Note: An entity with a health bar ({@link Player}, {@link EnderDragon},
	 * {@link Wither}, etc...} will have their bar scaled accordingly.
	 *
	 * @param health amount of health to set the maximum to
	 * @deprecated use {@link Attribute#GENERIC_MAX_HEALTH}.
	 */
	@Override @Deprecated
	public void setMaxHealth(double health) {
		this.maxHealth = health;
	}

	/**
	 * Resets the max health to the original amount.
	 * @deprecated use {@link Attribute#GENERIC_MAX_HEALTH}.
	 */
	@Override @Deprecated
	public void resetMaxHealth() {
		this.maxHealth = this.originalMaxHealth;
	}

	/**
	 * Launches a {@link Projectile} from the ProjectileSource.
	 *
	 * @param <T> a projectile subclass
	 * @param projectile class of the projectile to launch
	 * @return the launched projectile
	 */
	@Override @NotNull
	public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> projectile) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Launches a {@link Projectile} from the ProjectileSource with an
	 * initial velocity.
	 *
	 * @param <T> a projectile subclass
	 * @param projectile class of the projectile to launch
	 * @param velocity the velocity with which to launch
	 * @return the launched projectile
	 */
	@Override @NotNull
	public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> projectile,
	                                                 @Nullable Vector velocity) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
