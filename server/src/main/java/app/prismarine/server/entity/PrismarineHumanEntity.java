package app.prismarine.server.entity;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class PrismarineHumanEntity extends PrismarineLivingEntity implements HumanEntity {

	/**
	 * The {@link PlayerProfile} associated with the player
	 */
	protected final PlayerProfile profile;

	/**
	 * The current gamemode of the player
	 */
	private GameMode gameMode;

	public PrismarineHumanEntity(@NotNull Server server, @NotNull Location location, PlayerProfile profile) {
		super(server, location, 20);
		this.profile = profile;
	}

	/**
	 * Returns the name of this player
	 *
	 * @return Player name
	 */
	@Override @NotNull
	public String getName() {
		return Objects.requireNonNull(this.profile.getName());
	}

	/**
	 * Returns the UUID of this player
	 *
	 * @return Player UUID
	 */
	@Override @NotNull
	public UUID getUniqueId() {
		return Objects.requireNonNull(this.profile.getUniqueId());
	}

	/**
	 * Get the player's inventory.
	 *
	 * @return The inventory of the player, this also contains the armor
	 *     slots.
	 */
	@Override @NotNull
	public PlayerInventory getInventory() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the player's EnderChest inventory
	 *
	 * @return The EnderChest of the player
	 */
	@Override @NotNull
	public Inventory getEnderChest() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player's selected main hand
	 *
	 * @return the players main hand
	 */
	@Override @NotNull
	public MainHand getMainHand() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * If the player currently has an inventory window open, this method will
	 * set a property of that window, such as the state of a progress bar.
	 *
	 * @param prop The property.
	 * @param value The value to set the property to.
	 * @return True if the property was successfully set.
	 */
	@Override
	public boolean setWindowProperty(@NotNull InventoryView.Property prop, int value) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player's current enchantment seed.
	 *
	 * The Seed is used to generate enchantment options in the enchanting table
	 * for the player.
	 *
	 * @return the player's enchantment seed
	 */
	@Override
	public int getEnchantmentSeed() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the player's enchantment seed.
	 *
	 * The Seed is used to generate enchantment options in the enchanting table
	 * for the player.
	 *
	 * @param seed the player's new enchantment seed
	 */
	@Override
	public void setEnchantmentSeed(int seed) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the inventory view the player is currently viewing. If they do not
	 * have an inventory window open, it returns their internal crafting view.
	 *
	 * @return The inventory view.
	 */
	@Override @NotNull
	public InventoryView getOpenInventory() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Opens an inventory window with the specified inventory on the top and
	 * the player's inventory on the bottom.
	 *
	 * @param inventory The inventory to open
	 * @return The newly opened inventory view
	 */
	@Override @Nullable
	public InventoryView openInventory(@NotNull Inventory inventory) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Opens an empty workbench inventory window with the player's inventory
	 * on the bottom.
	 *
	 * @param location The location to attach it to. If null, the player's
	 *     location is used.
	 * @param force If false, and there is no workbench block at the location,
	 *     no inventory will be opened and null will be returned.
	 * @return The newly opened inventory view, or null if it could not be
	 *     opened.
	 */
	@Override @Nullable
	public InventoryView openWorkbench(@Nullable Location location, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Opens an empty enchanting inventory window with the player's inventory
	 * on the bottom.
	 *
	 * @param location The location to attach it to. If null, the player's
	 *     location is used.
	 * @param force If false, and there is no enchanting table at the
	 *     location, no inventory will be opened and null will be returned.
	 * @return The newly opened inventory view, or null if it could not be
	 *     opened.
	 */
	@Override @Nullable
	public InventoryView openEnchanting(@Nullable Location location, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Opens an inventory window to the specified inventory view.
	 *
	 * @param inventory The view to open
	 */
	@Override
	public void openInventory(@NotNull InventoryView inventory) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Starts a trade between the player and the villager.
	 *
	 * Note that only one player may trade with a villager at once. You must use
	 * the force parameter for this.
	 *
	 * @param trader The merchant to trade with. Cannot be null.
	 * @param force whether to force the trade even if another player is trading
	 * @return The newly opened inventory view, or null if it could not be
	 * opened.
	 */
	@Override @Nullable
	public InventoryView openMerchant(@NotNull Villager trader, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Starts a trade between the player and the merchant.
	 *
	 * Note that only one player may trade with a merchant at once. You must use
	 * the force parameter for this.
	 *
	 * @param merchant The merchant to trade with. Cannot be null.
	 * @param force whether to force the trade even if another player is trading
	 * @return The newly opened inventory view, or null if it could not be
	 * opened.
	 */
	@Override @Nullable
	public InventoryView openMerchant(@NotNull Merchant merchant, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Force-closes the currently open inventory view for this player, if any.
	 */
	@Override
	public void closeInventory() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the ItemStack currently in your hand, can be empty.
	 *
	 * @return The ItemStack of the item you are currently holding.
	 * @deprecated Humans may now dual wield in their off hand, use explicit
	 * methods in {@link PlayerInventory}.
	 */
	@Override @Deprecated @NotNull
	public ItemStack getItemInHand() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the item to the given ItemStack, this will replace whatever the
	 * user was holding.
	 *
	 * @param item The ItemStack which will end up in the hand
	 * @deprecated Humans may now dual wield in their off hand, use explicit
	 * methods in {@link PlayerInventory}.
	 */
	@Override @Deprecated
	public void setItemInHand(@Nullable ItemStack item) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the ItemStack currently on your cursor, can be empty. Will
	 * always be empty if the player currently has no open window.
	 *
	 * @return The ItemStack of the item you are currently moving around.
	 */
	@Override @NotNull
	public ItemStack getItemOnCursor() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the item to the given ItemStack, this will replace whatever the
	 * user was moving. Will always be empty if the player currently has no
	 * open window.
	 *
	 * @param item The ItemStack which will end up in the hand
	 */
	@Override
	public void setItemOnCursor(@Nullable ItemStack item) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Check whether a cooldown is active on the specified material.
	 *
	 * @param material the material to check
	 * @return if a cooldown is active on the material
	 * @throws IllegalArgumentException if the material is not an item
	 */
	@Override
	public boolean hasCooldown(@NotNull Material material) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the cooldown time in ticks remaining for the specified material.
	 *
	 * @param material the material to check
	 * @return the remaining cooldown time in ticks
	 * @throws IllegalArgumentException if the material is not an item
	 */
	@Override
	public int getCooldown(@NotNull Material material) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Set a cooldown on the specified material for a certain amount of ticks.
	 * ticks. 0 ticks will result in the removal of the cooldown.
	 * <p>
	 * Cooldowns are used by the server for items such as ender pearls and
	 * shields to prevent them from being used repeatedly.
	 * <p>
	 * Note that cooldowns will not by themselves stop an item from being used
	 * for attacking.
	 *
	 * @param material the material to set the cooldown for
	 * @param ticks the amount of ticks to set or 0 to remove
	 * @throws IllegalArgumentException if the material is not an item
	 */
	@Override
	public void setCooldown(@NotNull Material material, int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the sleep ticks of the player. This value may be capped.
	 *
	 * @return slumber ticks
	 */
	@Override
	public int getSleepTicks() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Attempts to make the entity sleep at the given location.
	 * <br>
	 * The location must be in the current world and have a bed placed at the
	 * location. The game may also enforce other requirements such as proximity
	 * to bed, monsters, and dimension type if force is not set.
	 *
	 * @param location the location of the bed
	 * @param force whether to try and sleep at the location even if not
	 * normally possible
	 * @return whether the sleep was successful
	 */
	@Override
	public boolean sleep(@NotNull Location location, boolean force) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Causes the player to wakeup if they are currently sleeping.
	 *
	 * @param setSpawnLocation whether to set their spawn location to the bed
	 * they are currently sleeping in
	 * @throws IllegalStateException if not sleeping
	 */
	@Override
	public void wakeup(boolean setSpawnLocation) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the location of the bed the player is currently sleeping in
	 *
	 * @return location
	 * @throws IllegalStateException if not sleeping
	 */
	@Override @NotNull
	public Location getBedLocation() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets this human's current {@link GameMode}
	 *
	 * @return Current game mode
	 */
	@Override @NotNull
	public GameMode getGameMode() {
		return this.gameMode;
	}

	/**
	 * Sets this human's current {@link GameMode}
	 *
	 * @param mode New game mode
	 */
	@Override
	public void setGameMode(@NotNull GameMode mode) {
		this.gameMode = mode;
	}

	/**
	 * Check if the player is currently blocking (ie with a shield).
	 *
	 * @return Whether they are blocking.
	 */
	@Override
	public boolean isBlocking() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Check if the player currently has their hand raised (ie about to begin
	 * blocking).
	 *
	 * @return Whether their hand is raised
	 */
	@Override
	public boolean isHandRaised() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the total amount of experience required for the player to level
	 *
	 * @return Experience required to level up
	 */
	@Override
	public int getExpToLevel() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the current cooldown for a player's attack.
	 *
	 * This is used to calculate damage, with 1.0 representing a fully charged
	 * attack and 0.0 representing a non-charged attack
	 *
	 * @return A float between 0.0-1.0 representing the progress of the charge
	 */
	@Override
	public float getAttackCooldown() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Discover a recipe for this player such that it has not already been
	 * discovered. This method will add the key's associated recipe to the
	 * player's recipe book.
	 *
	 * @param recipe the key of the recipe to discover
	 *
	 * @return whether or not the recipe was newly discovered
	 */
	@Override
	public boolean discoverRecipe(@NotNull NamespacedKey recipe) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Discover a collection of recipes for this player such that they have not
	 * already been discovered. This method will add the keys' associated
	 * recipes to the player's recipe book. If a recipe in the provided
	 * collection has already been discovered, it will be silently ignored.
	 *
	 * @param recipes the keys of the recipes to discover
	 *
	 * @return the amount of newly discovered recipes where 0 indicates that
	 * none were newly discovered and a number equal to {@code recipes.size()}
	 * indicates that all were new
	 */
	@Override
	public int discoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Undiscover a recipe for this player such that it has already been
	 * discovered. This method will remove the key's associated recipe from the
	 * player's recipe book.
	 *
	 * @param recipe the key of the recipe to undiscover
	 *
	 * @return whether or not the recipe was successfully undiscovered (i.e. it
	 * was previously discovered)
	 */
	@Override
	public boolean undiscoverRecipe(@NotNull NamespacedKey recipe) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Undiscover a collection of recipes for this player such that they have
	 * already been discovered. This method will remove the keys' associated
	 * recipes from the player's recipe book. If a recipe in the provided
	 * collection has not yet been discovered, it will be silently ignored.
	 *
	 * @param recipes the keys of the recipes to undiscover
	 *
	 * @return the amount of undiscovered recipes where 0 indicates that none
	 * were undiscovered and a number equal to {@code recipes.size()} indicates
	 * that all were undiscovered
	 */
	@Override
	public int undiscoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Check whether or not this entity has discovered the specified recipe.
	 *
	 * @param recipe the key of the recipe to check
	 *
	 * @return true if discovered, false otherwise
	 */
	@Override
	public boolean hasDiscoveredRecipe(@NotNull NamespacedKey recipe) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get an immutable set of recipes this entity has discovered.
	 *
	 * @return all discovered recipes
	 */
	@Override @NotNull
	public Set<NamespacedKey> getDiscoveredRecipes() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the entity currently perched on the left shoulder or null if no
	 * entity.
	 * <br>
	 * The returned entity will not be spawned within the world, so most
	 * operations are invalid unless the entity is first spawned in.
	 *
	 * @return left shoulder entity
	 * @deprecated There are currently no well defined semantics regarding
	 * serialized entities in Bukkit. Use with care.
	 */
	@Override @Deprecated @Nullable
	public Entity getShoulderEntityLeft() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the entity currently perched on the left shoulder, or null to
	 * remove. This method will remove the entity from the world.
	 * <br>
	 * Note that only a copy of the entity will be set to display on the
	 * shoulder.
	 * <br>
	 * Also note that the client will currently only render {@link Parrot}
	 * entities.
	 *
	 * @param entity left shoulder entity
	 * @deprecated There are currently no well defined semantics regarding
	 * serialized entities in Bukkit. Use with care.
	 */
	@Override @Deprecated
	public void setShoulderEntityLeft(@Nullable Entity entity) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the entity currently perched on the right shoulder or null if no
	 * entity.
	 * <br>
	 * The returned entity will not be spawned within the world, so most
	 * operations are invalid unless the entity is first spawned in.
	 *
	 * @return right shoulder entity
	 * @deprecated There are currently no well defined semantics regarding
	 * serialized entities in Bukkit. Use with care.
	 */
	@Override @Deprecated @Nullable
	public Entity getShoulderEntityRight() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the entity currently perched on the right shoulder, or null to
	 * remove. This method will remove the entity from the world.
	 * <br>
	 * Note that only a copy of the entity will be set to display on the
	 * shoulder.
	 * <br>
	 * Also note that the client will currently only render {@link Parrot}
	 * entities.
	 *
	 * @param entity right shoulder entity
	 * @deprecated There are currently no well defined semantics regarding
	 * serialized entities in Bukkit. Use with care.
	 */
	@Override @Deprecated
	public void setShoulderEntityRight(@Nullable Entity entity) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Make the entity drop the item in their hand.
	 * <br>
	 * This will force the entity to drop the item they are holding with
	 * an option to drop the entire {@link ItemStack} or just 1 of the items.
	 *
	 * @param dropAll True to drop entire stack, false to drop 1 of the stack
	 * @return True if item was dropped successfully
	 */
	@Override
	public boolean dropItem(boolean dropAll) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the players current exhaustion level.
	 * <p>
	 * Exhaustion controls how fast the food level drops. While you have a
	 * certain amount of exhaustion, your saturation will drop to zero, and
	 * then your food will drop to zero.
	 *
	 * @return Exhaustion level
	 */
	@Override
	public float getExhaustion() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the players current exhaustion level
	 *
	 * @param value Exhaustion level
	 */
	@Override
	public void setExhaustion(float value) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the players current saturation level.
	 * <p>
	 * Saturation is a buffer for food level. Your food level will not drop if
	 * you are saturated {@literal >} 0.
	 *
	 * @return Saturation level
	 */
	@Override
	public float getSaturation() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the players current saturation level
	 *
	 * @param value Saturation level
	 */
	@Override
	public void setSaturation(float value) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the players current food level
	 *
	 * @return Food level
	 */
	@Override
	public int getFoodLevel() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the players current food level
	 *
	 * @param value New food level
	 */
	@Override
	public void setFoodLevel(int value) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the regeneration rate (1 health per x ticks) of
	 * the HumanEntity when they have saturation and
	 * their food level is {@literal >=} 20. Default is 10.
	 *
	 * @return the regeneration rate
	 */
	@Override
	public int getSaturatedRegenRate() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Set the regeneration rate (1 health per x ticks) of
	 * the HumanEntity when they have saturation and
	 * their food level is {@literal >=} 20. Default is 10.
	 * Not affected if the world's difficulty is peaceful.
	 *
	 * @param ticks the amount of ticks to gain 1 health.
	 */
	@Override
	public void setSaturatedRegenRate(int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the regeneration rate (1 health per x ticks) of
	 * the HumanEntity when they have no saturation and
	 * their food level is {@literal >=} 18. Default is 80.
	 *
	 * @return the regeneration rate
	 */
	@Override
	public int getUnsaturatedRegenRate() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the regeneration rate (1 health per x ticks) of
	 * the HumanEntity when they have no saturation and
	 * their food level is {@literal >=} 18. Default is 80.
	 * Not affected if the world's difficulty is peaceful.
	 *
	 * @param ticks the amount of ticks to gain 1 health.
	 */
	@Override
	public void setUnsaturatedRegenRate(int ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the starvation rate (1 health per x ticks) of
	 * the HumanEntity. Default is 80.
	 *
	 * @return the starvation rate
	 */
	@Override
	public int getStarvationRate() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the starvation rate (1 health per x ticks) of
	 * the HumanEntity. Default is 80.
	 *
	 * @param ticks the amount of ticks to lose 1 health
	 */
	@Override
	public void setStarvationRate(int ticks) {
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
	 * Sets the player's last death location.
	 * <br>
	 * <b>Note:</b> This data is updated in the player's client only when the
	 * player respawns.
	 *
	 * @param location where to set the last death player location
	 */
	@Override
	public void setLastDeathLocation(@Nullable Location location) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Perform a firework boost.
	 * <p>
	 * This method will only work such that {@link #isGliding()} is true and
	 * the entity is actively gliding with an elytra. Additionally, the supplied
	 * {@code fireworkItemStack} must be a firework rocket. The power of the boost
	 * will directly correlate to {@link FireworkMeta#getPower()}.
	 *
	 * @param fireworkItemStack the firework item stack to use to glide
	 * @return the attached {@link Firework}, or null if the entity could not
	 * be boosted
	 * @throws IllegalArgumentException if the fireworkItemStack is not a firework
	 */
	@Override @Nullable
	public Firework fireworkBoost(@NotNull ItemStack fireworkItemStack) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
