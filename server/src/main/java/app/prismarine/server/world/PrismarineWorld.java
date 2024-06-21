package app.prismarine.server.world;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.entity.PrismarineEntity;
import app.prismarine.server.entity.PrismarinePlayer;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.ChannelNotRegisteredException;
import org.bukkit.plugin.messaging.MessageTooLargeException;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.util.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PrismarineWorld implements World {

	/**
	 * The {@link WorldCreator} that created this world
	 */
	@Getter
	private final WorldCreator creator;

	/**
	 * The {@link WorldProvider} for this world, responsible for abstracting reading/writing to disk
	 */
	@Getter
	private final WorldProvider provider;

	/**
	 * Random non-persistent UUID used to identify this world
	 */
	private final UUID uuid = UUID.randomUUID();

	/**
	 * The player spawn location of this world
	 */
	private Location spawnLocation = new Location(this, 0, 64, 0); // Default fallback

	/**
	 * A list of players in this world
	 */
	private final List<Player> players = new ArrayList<>();

	/**
	 * A list of entities in this world
	 */
	private final List<Entity> entities = new ArrayList<>();

	public PrismarineWorld(WorldCreator creator, WorldProvider provider) {
		this.creator = creator;
		this.provider = provider;
		((PrismarineServer) Bukkit.getServer()).getWorldManager().addWorld(this);
	}

	public void tick() {

		// Tick players TODO not thread safe throws CME with players joining!
		this.players.forEach(player -> {
			PrismarinePlayer prismarinePlayer = (PrismarinePlayer) player;

			if (prismarinePlayer.getConnection().isFinishedLogin()) {
				prismarinePlayer.tick();
			}
		});

		// Mark entities as having been updated (ticked)
		this.entities.forEach(entity -> {
			PrismarineEntity prismarineEntity = (PrismarineEntity) entity;
			prismarineEntity.setRotated(false);
			prismarineEntity.setMoved(false);
		});
	}

	/**
	 * Return the namespaced identifier for this object.
	 *
	 * @return this object's key
	 */
	@Override
	public @NotNull NamespacedKey getKey() {
		return null;
	}

	/**
	 * Gets the {@link Biome} at the given {@link Location}.
	 *
	 * @param location the location of the biome
	 * @return Biome at the given location
	 */
	@Override
	public @NotNull Biome getBiome(@NotNull Location location) {
		return null;
	}

	/**
	 * Gets the {@link Biome} at the given coordinates.
	 *
	 * @param x X-coordinate of the block
	 * @param y Y-coordinate of the block
	 * @param z Z-coordinate of the block
	 * @return Biome at the given coordinates
	 */
	@Override
	public @NotNull Biome getBiome(int x, int y, int z) {
		return null;
	}

	/**
	 * Sets the {@link Biome} at the given {@link Location}.
	 *
	 * @param location the location of the biome
	 * @param biome    New Biome type for this block
	 */
	@Override
	public void setBiome(@NotNull Location location, @NotNull Biome biome) {

	}

	/**
	 * Sets the {@link Biome} for the given block coordinates
	 *
	 * @param x     X-coordinate of the block
	 * @param y     Y-coordinate of the block
	 * @param z     Z-coordinate of the block
	 * @param biome New Biome type for this block
	 */
	@Override
	public void setBiome(int x, int y, int z, @NotNull Biome biome) {

	}

	/**
	 * Gets the {@link BlockState} at the given {@link Location}.
	 *
	 * @param location The location of the block state
	 * @return Block state at the given location
	 */
	@Override
	public @NotNull BlockState getBlockState(@NotNull Location location) {
		return null;
	}

	/**
	 * Gets the {@link BlockState} at the given coordinates.
	 *
	 * @param x X-coordinate of the block state
	 * @param y Y-coordinate of the block state
	 * @param z Z-coordinate of the block state
	 * @return Block state at the given coordinates
	 */
	@Override
	public @NotNull BlockState getBlockState(int x, int y, int z) {
		return null;
	}

	/**
	 * Gets the {@link BlockData} at the given {@link Location}.
	 *
	 * @param location The location of the block data
	 * @return Block data at the given location
	 */
	@Override
	public @NotNull BlockData getBlockData(@NotNull Location location) {
		return null;
	}

	/**
	 * Gets the {@link BlockData} at the given coordinates.
	 *
	 * @param x X-coordinate of the block data
	 * @param y Y-coordinate of the block data
	 * @param z Z-coordinate of the block data
	 * @return Block data at the given coordinates
	 */
	@Override
	public @NotNull BlockData getBlockData(int x, int y, int z) {
		return null;
	}

	/**
	 * Gets the type of the block at the given {@link Location}.
	 *
	 * @param location The location of the block
	 * @return Material at the given coordinates
	 */
	@Override
	public @NotNull Material getType(@NotNull Location location) {
		return null;
	}

	/**
	 * Gets the type of the block at the given coordinates.
	 *
	 * @param x X-coordinate of the block
	 * @param y Y-coordinate of the block
	 * @param z Z-coordinate of the block
	 * @return Material at the given coordinates
	 */
	@Override
	public @NotNull Material getType(int x, int y, int z) {
		return null;
	}

	/**
	 * Sets the {@link BlockData} at the given {@link Location}.
	 *
	 * @param location  The location of the block
	 * @param blockData The block data to set the block to
	 */
	@Override
	public void setBlockData(@NotNull Location location, @NotNull BlockData blockData) {

	}

	/**
	 * Sets the {@link BlockData} at the given coordinates.
	 *
	 * @param x         X-coordinate of the block
	 * @param y         Y-coordinate of the block
	 * @param z         Z-coordinate of the block
	 * @param blockData The block data to set the block to
	 */
	@Override
	public void setBlockData(int x, int y, int z, @NotNull BlockData blockData) {

	}

	/**
	 * Sets the {@link Material} at the given {@link Location}.
	 *
	 * @param location The location of the block
	 * @param material The type to set the block to
	 */
	@Override
	public void setType(@NotNull Location location, @NotNull Material material) {

	}

	/**
	 * Sets the {@link Material} at the given coordinates.
	 *
	 * @param x        X-coordinate of the block
	 * @param y        Y-coordinate of the block
	 * @param z        Z-coordinate of the block
	 * @param material The type to set the block to
	 */
	@Override
	public void setType(int x, int y, int z, @NotNull Material material) {

	}

	/**
	 * Creates a tree at the given {@link Location}
	 *
	 * @param location Location to spawn the tree
	 * @param random   Random to use to generated the tree
	 * @param type     Type of the tree to create
	 * @return true if the tree was created successfully, otherwise false
	 */
	@Override
	public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type) {
		return false;
	}

	/**
	 * Creates a tree at the given {@link Location}
	 * <p>
	 * The provided consumer gets called for every block which gets changed
	 * as a result of the tree generation. When the consumer gets called no
	 * modifications to the world are done yet. Which means, that calling
	 * {@link #getBlockState(Location)} in the consumer while return the state
	 * of the block before the generation.
	 * <p>
	 * Modifications done to the {@link BlockState} in the consumer are respected,
	 * which means that it is not necessary to call {@link BlockState#update()}
	 *
	 * @param location      Location to spawn the tree
	 * @param random        Random to use to generated the tree
	 * @param type          Type of the tree to create
	 * @param stateConsumer The consumer which should get called for every block which gets changed
	 * @return true if the tree was created successfully, otherwise false
	 */
	@Override
	public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type,
	                            @Nullable Consumer<? super BlockState> stateConsumer) {
		return false;
	}

	/**
	 * Creates a tree at the given {@link Location}
	 * <p>
	 * The provided predicate gets called for every block which gets changed
	 * as a result of the tree generation. When the predicate gets called no
	 * modifications to the world are done yet. Which means, that calling
	 * {@link #getBlockState(Location)} in the predicate will return the state
	 * of the block before the generation.
	 * <p>
	 * If the predicate returns {@code true} the block gets set in the world.
	 * If it returns {@code false} the block won't get set in the world.
	 *
	 * @param location       Location to spawn the tree
	 * @param random         Random to use to generated the tree
	 * @param type           Type of the tree to create
	 * @param statePredicate The predicate which should get used to test if a block should be set or not.
	 * @return true if the tree was created successfully, otherwise false
	 */
	@Override
	public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type,
	                            @Nullable Predicate<? super BlockState> statePredicate) {
		return false;
	}

	/**
	 * Creates a entity at the given {@link Location}
	 *
	 * @param location The location to spawn the entity
	 * @param type     The entity to spawn
	 * @return Resulting Entity of this method
	 */
	@Override
	public @NotNull Entity spawnEntity(@NotNull Location location, @NotNull EntityType type) {
		return null;
	}

	/**
	 * Creates a new entity at the given {@link Location}.
	 *
	 * @param loc           the location at which the entity will be spawned.
	 * @param type          the entity type that determines the entity to spawn.
	 * @param randomizeData whether or not the entity's data should be randomised
	 *                      before spawning. By default entities are randomised
	 *                      before spawning in regards to their equipment, age,
	 *                      attributes, etc.
	 *                      An example of this randomization would be the color of
	 *                      a sheep, random enchantments on the equipment of mobs
	 *                      or even a zombie becoming a chicken jockey.
	 *                      If set to false, the entity will not be randomised
	 *                      before spawning, meaning all their data will remain
	 *                      in their default state and not further modifications
	 *                      to the entity will be made.
	 *                      Notably only entities that extend the
	 *                      {@link Mob} interface provide
	 *                      randomisation logic for their spawn.
	 *                      This parameter is hence useless for any other type
	 *                      of entity.
	 * @return the spawned entity instance.
	 */
	@Override
	public @NotNull Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type, boolean randomizeData) {
		return null;
	}

	/**
	 * Creates an entity of a specific class at the given {@link Location} but
	 * does not spawn it in the world.
	 * <p>
	 * <b>Note:</b> The created entity keeps a reference to the world it was
	 * created in, care should be taken that the entity does not outlive the
	 * world instance as this will lead to memory leaks.
	 *
	 * @param location the {@link Location} to create the entity at
	 * @param clazz    the class of the {@link Entity} to spawn
	 * @return an instance of the created {@link Entity}
	 * @see #addEntity(Entity)
	 * @see Entity#createSnapshot()
	 */
	@Override
	public <T extends Entity> @NotNull T createEntity(@NotNull Location location, @NotNull Class<T> clazz) {
		return null;
	}

	/**
	 * Spawn an entity of a specific class at the given {@link Location}
	 *
	 * @param location the {@link Location} to spawn the entity at
	 * @param clazz    the class of the {@link Entity} to spawn
	 * @return an instance of the spawned {@link Entity}
	 * @throws IllegalArgumentException if either parameter is null or the
	 *                                  {@link Entity} requested cannot be spawned
	 */
	@Override
	public <T extends Entity> @NotNull T spawn(@NotNull Location location,
	                                           @NotNull Class<T> clazz) throws IllegalArgumentException {
		return null;
	}

	/**
	 * Spawn an entity of a specific class at the given {@link Location}, with
	 * the supplied function run before the entity is added to the world.
	 * <br>
	 * Note that when the function is run, the entity will not be actually in
	 * the world. Any operation involving such as teleporting the entity is undefined
	 * until after this function returns.
	 *
	 * @param location the {@link Location} to spawn the entity at
	 * @param clazz    the class of the {@link Entity} to spawn
	 * @param function the function to be run before the entity is spawned.
	 * @return an instance of the spawned {@link Entity}
	 * @throws IllegalArgumentException if either parameter is null or the
	 *                                  {@link Entity} requested cannot be spawned
	 */
	@Override
	public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz,
	                                           @Nullable Consumer<? super T> function) throws IllegalArgumentException {
		return null;
	}

	/**
	 * Creates a new entity at the given {@link Location} with the supplied
	 * function run before the entity is added to the world.
	 * <br>
	 * Note that when the function is run, the entity will not be actually in
	 * the world. Any operation involving such as teleporting the entity is undefined
	 * until after this function returns.
	 * The passed function however is run after the potential entity's spawn
	 * randomization and hence already allows access to the values of the mob,
	 * whether or not those were randomized, such as attributes or the entity
	 * equipment.
	 *
	 * @param location      the location at which the entity will be spawned.
	 * @param clazz         the class of the {@link Entity} that is to be spawned.
	 * @param randomizeData whether or not the entity's data should be randomised
	 *                      before spawning. By default entities are randomised
	 *                      before spawning in regards to their equipment, age,
	 *                      attributes, etc.
	 *                      An example of this randomization would be the color of
	 *                      a sheep, random enchantments on the equipment of mobs
	 *                      or even a zombie becoming a chicken jockey.
	 *                      If set to false, the entity will not be randomised
	 *                      before spawning, meaning all their data will remain
	 *                      in their default state and not further modifications
	 *                      to the entity will be made.
	 *                      Notably only entities that extend the
	 *                      {@link Mob} interface provide
	 *                      randomisation logic for their spawn.
	 *                      This parameter is hence useless for any other type
	 *                      of entity.
	 * @param function      the function to be run before the entity is spawned.
	 * @return the spawned entity instance.
	 * @throws IllegalArgumentException if either the world or clazz parameter are null.
	 */
	@Override
	public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz,
	                                           boolean randomizeData, @Nullable Consumer<? super T> function) throws IllegalArgumentException {
		return null;
	}

	/**
	 * Gets the highest non-empty (impassable) coordinate at the given
	 * coordinates.
	 *
	 * @param x X-coordinate of the blocks
	 * @param z Z-coordinate of the blocks
	 * @return Y-coordinate of the highest non-empty block
	 */
	@Override
	public int getHighestBlockYAt(int x, int z) {
		return 0;
	}

	/**
	 * Gets the highest non-empty (impassable) coordinate at the given
	 * {@link Location}.
	 *
	 * @param location Location of the blocks
	 * @return Y-coordinate of the highest non-empty block
	 */
	@Override
	public int getHighestBlockYAt(@NotNull Location location) {
		return 0;
	}

	/**
	 * Gets the highest coordinate corresponding to the {@link HeightMap} at the
	 * given coordinates.
	 *
	 * @param x         X-coordinate of the blocks
	 * @param z         Z-coordinate of the blocks
	 * @param heightMap the heightMap that is used to determine the highest
	 *                  point
	 * @return Y-coordinate of the highest block corresponding to the
	 * {@link HeightMap}
	 */
	@Override
	public int getHighestBlockYAt(int x, int z, @NotNull HeightMap heightMap) {
		return 0;
	}

	/**
	 * Gets the highest coordinate corresponding to the {@link HeightMap} at the
	 * given {@link Location}.
	 *
	 * @param location  Location of the blocks
	 * @param heightMap the heightMap that is used to determine the highest
	 *                  point
	 * @return Y-coordinate of the highest block corresponding to the
	 * {@link HeightMap}
	 */
	@Override
	public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightMap heightMap) {
		return 0;
	}

	/**
	 * Spawns a previously created entity in the world. <br>
	 * The provided entity must not have already been spawned in a world.
	 *
	 * @param entity the entity to add
	 * @return the entity now in the world
	 */
	@Override
	public <T extends Entity> @NotNull T addEntity(@NotNull T entity) {

		if (entity instanceof PrismarinePlayer player) {
			this.players.add(player);
		}

		// todo update entity world? idk

		this.entities.add(entity);
		return entity;
	}

	/**
	 * Gets the {@link Block} at the given coordinates
	 *
	 * @param x X-coordinate of the block
	 * @param y Y-coordinate of the block
	 * @param z Z-coordinate of the block
	 * @return Block at the given coordinates
	 */
	@Override
	public @NotNull Block getBlockAt(int x, int y, int z) {
		return null;
	}

	/**
	 * Gets the {@link Block} at the given {@link Location}
	 *
	 * @param location Location of the block
	 * @return Block at the given location
	 */
	@Override
	public @NotNull Block getBlockAt(@NotNull Location location) {
		return null;
	}

	/**
	 * Gets the highest non-empty (impassable) block at the given coordinates.
	 *
	 * @param x X-coordinate of the block
	 * @param z Z-coordinate of the block
	 * @return Highest non-empty block
	 */
	@Override
	public @NotNull Block getHighestBlockAt(int x, int z) {
		return null;
	}

	/**
	 * Gets the highest non-empty (impassable) block at the given coordinates.
	 *
	 * @param location Coordinates to get the highest block
	 * @return Highest non-empty block
	 */
	@Override
	public @NotNull Block getHighestBlockAt(@NotNull Location location) {
		return null;
	}

	/**
	 * Gets the highest block corresponding to the {@link HeightMap} at the
	 * given coordinates.
	 *
	 * @param x         X-coordinate of the block
	 * @param z         Z-coordinate of the block
	 * @param heightMap the heightMap that is used to determine the highest
	 *                  point
	 * @return Highest block corresponding to the {@link HeightMap}
	 */
	@Override
	public @NotNull Block getHighestBlockAt(int x, int z, @NotNull HeightMap heightMap) {
		return null;
	}

	/**
	 * Gets the highest block corresponding to the {@link HeightMap} at the
	 * given coordinates.
	 *
	 * @param location  Coordinates to get the highest block
	 * @param heightMap the heightMap that is used to determine the highest
	 *                  point
	 * @return Highest block corresponding to the {@link HeightMap}
	 */
	@Override
	public @NotNull Block getHighestBlockAt(@NotNull Location location, @NotNull HeightMap heightMap) {
		return null;
	}

	/**
	 * Gets the {@link Chunk} at the given coordinates
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 * @return Chunk at the given coordinates
	 */
	@Override
	public @NotNull Chunk getChunkAt(int x, int z) {
		return null;
	}

	/**
	 * Gets the {@link Chunk} at the given coordinates
	 *
	 * @param x        X-coordinate of the chunk
	 * @param z        Z-coordinate of the chunk
	 * @param generate Whether the chunk should be fully generated or not
	 * @return Chunk at the given coordinates
	 */
	@Override
	public @NotNull Chunk getChunkAt(int x, int z, boolean generate) {
		return null;
	}

	/**
	 * Gets the {@link Chunk} at the given {@link Location}
	 *
	 * @param location Location of the chunk
	 * @return Chunk at the given location
	 */
	@Override
	public @NotNull Chunk getChunkAt(@NotNull Location location) {
		return null;
	}

	/**
	 * Gets the {@link Chunk} that contains the given {@link Block}
	 *
	 * @param block Block to get the containing chunk from
	 * @return The chunk that contains the given block
	 */
	@Override
	public @NotNull Chunk getChunkAt(@NotNull Block block) {
		return null;
	}

	/**
	 * Checks if the specified {@link Chunk} is loaded
	 *
	 * @param chunk The chunk to check
	 * @return true if the chunk is loaded, otherwise false
	 */
	@Override
	public boolean isChunkLoaded(@NotNull Chunk chunk) {
		return false;
	}

	/**
	 * Gets an array of all loaded {@link Chunk}s
	 *
	 * @return Chunk[] containing all loaded chunks
	 */
	@Override
	public @NotNull Chunk[] getLoadedChunks() {
		return new Chunk[0];
	}

	/**
	 * Loads the specified {@link Chunk}.
	 * <p>
	 * <b>This method will keep the specified chunk loaded until one of the
	 * unload methods is manually called. Callers are advised to instead use
	 * getChunkAt which will only temporarily load the requested chunk.</b>
	 *
	 * @param chunk The chunk to load
	 */
	@Override
	public void loadChunk(@NotNull Chunk chunk) {

	}

	/**
	 * Checks if the {@link Chunk} at the specified coordinates is loaded
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 * @return true if the chunk is loaded, otherwise false
	 */
	@Override
	public boolean isChunkLoaded(int x, int z) {
		return false;
	}

	/**
	 * Checks if the {@link Chunk} at the specified coordinates is generated
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 * @return true if the chunk is generated, otherwise false
	 */
	@Override
	public boolean isChunkGenerated(int x, int z) {
		return false;
	}

	/**
	 * Checks if the {@link Chunk} at the specified coordinates is loaded and
	 * in use by one or more players
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 * @return true if the chunk is loaded and in use by one or more players,
	 * otherwise false
	 * @deprecated This method was added to facilitate chunk garbage collection.
	 * As of the current Minecraft version chunks are now strictly managed and
	 * will not be loaded for more than 1 tick unless they are in use.
	 */
	@Override
	public boolean isChunkInUse(int x, int z) {
		return false;
	}

	/**
	 * Loads the {@link Chunk} at the specified coordinates.
	 * <p>
	 * <b>This method will keep the specified chunk loaded until one of the
	 * unload methods is manually called. Callers are advised to instead use
	 * getChunkAt which will only temporarily load the requested chunk.</b>
	 * <p>
	 * If the chunk does not exist, it will be generated.
	 * <p>
	 * This method is analogous to {@link #loadChunk(int, int, boolean)} where
	 * generate is true.
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 */
	@Override
	public void loadChunk(int x, int z) {

	}

	/**
	 * Loads the {@link Chunk} at the specified coordinates.
	 * <p>
	 * <b>This method will keep the specified chunk loaded until one of the
	 * unload methods is manually called. Callers are advised to instead use
	 * getChunkAt which will only temporarily load the requested chunk.</b>
	 *
	 * @param x        X-coordinate of the chunk
	 * @param z        Z-coordinate of the chunk
	 * @param generate Whether or not to generate a chunk if it doesn't
	 *                 already exist
	 * @return true if the chunk has loaded successfully, otherwise false
	 */
	@Override
	public boolean loadChunk(int x, int z, boolean generate) {
		return false;
	}

	/**
	 * Safely unloads and saves the {@link Chunk} at the specified coordinates
	 * <p>
	 * This method is analogous to {@link #unloadChunk(int, int, boolean)}
	 * where save is true.
	 *
	 * @param chunk the chunk to unload
	 * @return true if the chunk has unloaded successfully, otherwise false
	 */
	@Override
	public boolean unloadChunk(@NotNull Chunk chunk) {
		return false;
	}

	/**
	 * Safely unloads and saves the {@link Chunk} at the specified coordinates
	 * <p>
	 * This method is analogous to {@link #unloadChunk(int, int, boolean)}
	 * where save is true.
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 * @return true if the chunk has unloaded successfully, otherwise false
	 */
	@Override
	public boolean unloadChunk(int x, int z) {
		return false;
	}

	/**
	 * Safely unloads and optionally saves the {@link Chunk} at the specified
	 * coordinates.
	 *
	 * @param x    X-coordinate of the chunk
	 * @param z    Z-coordinate of the chunk
	 * @param save Whether or not to save the chunk
	 * @return true if the chunk has unloaded successfully, otherwise false
	 */
	@Override
	public boolean unloadChunk(int x, int z, boolean save) {
		return false;
	}

	/**
	 * Safely queues the {@link Chunk} at the specified coordinates for
	 * unloading.
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 * @return true is the queue attempt was successful, otherwise false
	 */
	@Override
	public boolean unloadChunkRequest(int x, int z) {
		return false;
	}

	/**
	 * Regenerates the {@link Chunk} at the specified coordinates
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 * @return Whether the chunk was actually regenerated
	 * @deprecated regenerating a single chunk is not likely to produce the same
	 * chunk as before as terrain decoration may be spread across chunks. Use of
	 * this method should be avoided as it is known to produce buggy results.
	 */
	@Override
	public boolean regenerateChunk(int x, int z) {
		return false;
	}

	/**
	 * Resends the {@link Chunk} to all clients
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 * @return Whether the chunk was actually refreshed
	 * @deprecated This method is not guaranteed to work suitably across all client implementations.
	 */
	@Override
	public boolean refreshChunk(int x, int z) {
		return false;
	}

	/**
	 * Gets whether the chunk at the specified chunk coordinates is force
	 * loaded.
	 * <p>
	 * A force loaded chunk will not be unloaded due to lack of player activity.
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 * @return force load status
	 */
	@Override
	public boolean isChunkForceLoaded(int x, int z) {
		return false;
	}

	/**
	 * Sets whether the chunk at the specified chunk coordinates is force
	 * loaded.
	 * <p>
	 * A force loaded chunk will not be unloaded due to lack of player activity.
	 *
	 * @param x      X-coordinate of the chunk
	 * @param z      Z-coordinate of the chunk
	 * @param forced force load status
	 */
	@Override
	public void setChunkForceLoaded(int x, int z, boolean forced) {

	}

	/**
	 * Returns all force loaded chunks in this world.
	 * <p>
	 * A force loaded chunk will not be unloaded due to lack of player activity.
	 *
	 * @return unmodifiable collection of force loaded chunks
	 */
	@Override
	public @NotNull Collection<Chunk> getForceLoadedChunks() {
		return null;
	}

	/**
	 * Adds a plugin ticket for the specified chunk, loading the chunk if it is
	 * not already loaded.
	 * <p>
	 * A plugin ticket will prevent a chunk from unloading until it is
	 * explicitly removed. A plugin instance may only have one ticket per chunk,
	 * but each chunk can have multiple plugin tickets.
	 * </p>
	 *
	 * @param x      X-coordinate of the chunk
	 * @param z      Z-coordinate of the chunk
	 * @param plugin Plugin which owns the ticket
	 * @return {@code true} if a plugin ticket was added, {@code false} if the
	 * ticket already exists for the plugin
	 * @throws IllegalStateException If the specified plugin is not enabled
	 * @see #removePluginChunkTicket(int, int, Plugin)
	 */
	@Override
	public boolean addPluginChunkTicket(int x, int z, @NotNull Plugin plugin) {
		return false;
	}

	/**
	 * Removes the specified plugin's ticket for the specified chunk
	 * <p>
	 * A plugin ticket will prevent a chunk from unloading until it is
	 * explicitly removed. A plugin instance may only have one ticket per chunk,
	 * but each chunk can have multiple plugin tickets.
	 * </p>
	 *
	 * @param x      X-coordinate of the chunk
	 * @param z      Z-coordinate of the chunk
	 * @param plugin Plugin which owns the ticket
	 * @return {@code true} if the plugin ticket was removed, {@code false} if
	 * there is no plugin ticket for the chunk
	 * @see #addPluginChunkTicket(int, int, Plugin)
	 */
	@Override
	public boolean removePluginChunkTicket(int x, int z, @NotNull Plugin plugin) {
		return false;
	}

	/**
	 * Removes all plugin tickets for the specified plugin
	 * <p>
	 * A plugin ticket will prevent a chunk from unloading until it is
	 * explicitly removed. A plugin instance may only have one ticket per chunk,
	 * but each chunk can have multiple plugin tickets.
	 * </p>
	 *
	 * @param plugin Specified plugin
	 * @see #addPluginChunkTicket(int, int, Plugin)
	 * @see #removePluginChunkTicket(int, int, Plugin)
	 */
	@Override
	public void removePluginChunkTickets(@NotNull Plugin plugin) {

	}

	/**
	 * Retrieves a collection specifying which plugins have tickets for the
	 * specified chunk. This collection is not updated when plugin tickets are
	 * added or removed to the chunk.
	 * <p>
	 * A plugin ticket will prevent a chunk from unloading until it is
	 * explicitly removed. A plugin instance may only have one ticket per chunk,
	 * but each chunk can have multiple plugin tickets.
	 * </p>
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 * @return unmodifiable collection containing which plugins have tickets for
	 * the chunk
	 * @see #addPluginChunkTicket(int, int, Plugin)
	 * @see #removePluginChunkTicket(int, int, Plugin)
	 */
	@Override
	public @NotNull Collection<Plugin> getPluginChunkTickets(int x, int z) {
		return null;
	}

	/**
	 * Returns a map of which plugins have tickets for what chunks. The returned
	 * map is not updated when plugin tickets are added or removed to chunks. If
	 * a plugin has no tickets, it will be absent from the map.
	 * <p>
	 * A plugin ticket will prevent a chunk from unloading until it is
	 * explicitly removed. A plugin instance may only have one ticket per chunk,
	 * but each chunk can have multiple plugin tickets.
	 * </p>
	 *
	 * @return unmodifiable map containing which plugins have tickets for what
	 * chunks
	 * @see #addPluginChunkTicket(int, int, Plugin)
	 * @see #removePluginChunkTicket(int, int, Plugin)
	 */
	@Override
	public @NotNull Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
		return null;
	}

	/**
	 * Gets all Chunks intersecting the given BoundingBox.
	 *
	 * @param box BoundingBox to check
	 * @return A collection of Chunks intersecting the given BoundingBox
	 */
	@Override
	public @NotNull Collection<Chunk> getIntersectingChunks(@NotNull BoundingBox box) {
		return null;
	}

	/**
	 * Drops an item at the specified {@link Location}
	 *
	 * @param location Location to drop the item
	 * @param item     ItemStack to drop
	 * @return ItemDrop entity created as a result of this method
	 */
	@Override
	public @NotNull Item dropItem(@NotNull Location location, @NotNull ItemStack item) {
		return null;
	}

	/**
	 * Drops an item at the specified {@link Location}
	 * Note that functions will run before the entity is spawned
	 *
	 * @param location Location to drop the item
	 * @param item     ItemStack to drop
	 * @param function the function to be run before the entity is spawned.
	 * @return ItemDrop entity created as a result of this method
	 */
	@Override
	public @NotNull Item dropItem(@NotNull Location location, @NotNull ItemStack item,
	                              @Nullable Consumer<? super Item> function) {
		return null;
	}

	/**
	 * Drops an item at the specified {@link Location} with a random offset
	 *
	 * @param location Location to drop the item
	 * @param item     ItemStack to drop
	 * @return ItemDrop entity created as a result of this method
	 */
	@Override
	public @NotNull Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item) {
		return null;
	}

	/**
	 * Drops an item at the specified {@link Location} with a random offset
	 * Note that functions will run before the entity is spawned
	 *
	 * @param location Location to drop the item
	 * @param item     ItemStack to drop
	 * @param function the function to be run before the entity is spawned.
	 * @return ItemDrop entity created as a result of this method
	 */
	@Override
	public @NotNull Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item,
	                                       @Nullable Consumer<? super Item> function) {
		return null;
	}

	/**
	 * Creates an {@link Arrow} entity at the given {@link Location}
	 *
	 * @param location  Location to spawn the arrow
	 * @param direction Direction to shoot the arrow in
	 * @param speed     Speed of the arrow. A recommend speed is 0.6
	 * @param spread    Spread of the arrow. A recommend spread is 12
	 * @return Arrow entity spawned as a result of this method
	 */
	@Override
	public @NotNull Arrow spawnArrow(@NotNull Location location, @NotNull Vector direction,
	                                 float speed, float spread) {
		return null;
	}

	/**
	 * Creates an arrow entity of the given class at the given {@link Location}
	 *
	 * @param location  Location to spawn the arrow
	 * @param direction Direction to shoot the arrow in
	 * @param speed     Speed of the arrow. A recommend speed is 0.6
	 * @param spread    Spread of the arrow. A recommend spread is 12
	 * @param clazz     the Entity class for the arrow
	 *                  {@link SpectralArrow},{@link Arrow},{@link TippedArrow}
	 * @return Arrow entity spawned as a result of this method
	 */
	@Override
	public <T extends AbstractArrow> @NotNull T spawnArrow(@NotNull Location location, @NotNull Vector direction,
	                                                       float speed, float spread, @NotNull Class<T> clazz) {
		return null;
	}

	/**
	 * Creates a tree at the given {@link Location}
	 *
	 * @param location Location to spawn the tree
	 * @param type     Type of the tree to create
	 * @return true if the tree was created successfully, otherwise false
	 */
	@Override
	public boolean generateTree(@NotNull Location location, @NotNull TreeType type) {
		return false;
	}

	/**
	 * Creates a tree at the given {@link Location}
	 *
	 * @param loc      Location to spawn the tree
	 * @param type     Type of the tree to create
	 * @param delegate A class to call for each block changed as a result of
	 *                 this method
	 * @return true if the tree was created successfully, otherwise false
	 * @see #generateTree(Location, Random, TreeType, Consumer)
	 * @deprecated this method does not handle tile entities (bee nests)
	 */
	@Override
	public boolean generateTree(@NotNull Location loc, @NotNull TreeType type,
	                            @NotNull BlockChangeDelegate delegate) {
		return false;
	}

	/**
	 * Strikes lightning at the given {@link Location}
	 *
	 * @param loc The location to strike lightning
	 * @return The lightning entity.
	 */
	@Override
	public @NotNull LightningStrike strikeLightning(@NotNull Location loc) {
		return null;
	}

	/**
	 * Strikes lightning at the given {@link Location} without doing damage
	 *
	 * @param loc The location to strike lightning
	 * @return The lightning entity.
	 */
	@Override
	public @NotNull LightningStrike strikeLightningEffect(@NotNull Location loc) {
		return null;
	}

	/**
	 * Get a list of all entities in this World
	 *
	 * @return A List of all Entities currently residing in this world
	 */
	@Override
	public @NotNull List<Entity> getEntities() {
		return this.entities;
	}

	/**
	 * Get a list of all living entities in this World
	 *
	 * @return A List of all LivingEntities currently residing in this world
	 */
	@Override
	public @NotNull List<LivingEntity> getLivingEntities() {
		return null;
	}

	/**
	 * Get a collection of all entities in this World matching the given
	 * class/interface
	 *
	 * @param classes The classes representing the types of entity to match
	 * @return A List of all Entities currently residing in this world that
	 * match the given class/interface
	 */
	@Override
	public @NotNull <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T>... classes) {
		return null;
	}

	/**
	 * Get a collection of all entities in this World matching the given
	 * class/interface
	 *
	 * @param cls The class representing the type of entity to match
	 * @return A List of all Entities currently residing in this world that
	 * match the given class/interface
	 */
	@Override
	public @NotNull <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> cls) {
		return null;
	}

	/**
	 * Get a collection of all entities in this World matching any of the
	 * given classes/interfaces
	 *
	 * @param classes The classes representing the types of entity to match
	 * @return A List of all Entities currently residing in this world that
	 * match one or more of the given classes/interfaces
	 */
	@Override
	public @NotNull Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes) {
		return null;
	}

	/**
	 * Get a list of all players in this World
	 *
	 * @return A list of all Players currently residing in this world
	 */
	@Override
	public @NotNull List<Player> getPlayers() {
		return this.players;
	}

	/**
	 * Returns a list of entities within a bounding box centered around a
	 * Location.
	 * <p>
	 * This may not consider entities in currently unloaded chunks. Some
	 * implementations may impose artificial restrictions on the size of the
	 * search bounding box.
	 *
	 * @param location The center of the bounding box
	 * @param x        1/2 the size of the box along x axis
	 * @param y        1/2 the size of the box along y axis
	 * @param z        1/2 the size of the box along z axis
	 * @return the collection of entities near location. This will always be a
	 * non-null collection.
	 */
	@Override
	public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z) {
		return null;
	}

	/**
	 * Returns a list of entities within a bounding box centered around a
	 * Location.
	 * <p>
	 * This may not consider entities in currently unloaded chunks. Some
	 * implementations may impose artificial restrictions on the size of the
	 * search bounding box.
	 *
	 * @param location The center of the bounding box
	 * @param x        1/2 the size of the box along x axis
	 * @param y        1/2 the size of the box along y axis
	 * @param z        1/2 the size of the box along z axis
	 * @param filter   only entities that fulfill this predicate are considered,
	 *                 or <code>null</code> to consider all entities
	 * @return the collection of entities near location. This will always be a
	 * non-null collection.
	 */
	@Override
	public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y,
	                                                     double z, @Nullable Predicate<? super Entity> filter) {
		return null;
	}

	/**
	 * Returns a list of entities within the given bounding box.
	 * <p>
	 * This may not consider entities in currently unloaded chunks. Some
	 * implementations may impose artificial restrictions on the size of the
	 * search bounding box.
	 *
	 * @param boundingBox the bounding box
	 * @return the collection of entities within the bounding box, will always
	 * be a non-null collection
	 */
	@Override
	public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {
		return null;
	}

	/**
	 * Returns a list of entities within the given bounding box.
	 * <p>
	 * This may not consider entities in currently unloaded chunks. Some
	 * implementations may impose artificial restrictions on the size of the
	 * search bounding box.
	 *
	 * @param boundingBox the bounding box
	 * @param filter      only entities that fulfill this predicate are considered,
	 *                    or <code>null</code> to consider all entities
	 * @return the collection of entities within the bounding box, will always
	 * be a non-null collection
	 */
	@Override
	public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox,
	                                                     @Nullable Predicate<? super Entity> filter) {
		return null;
	}

	/**
	 * Performs a ray trace that checks for entity collisions.
	 * <p>
	 * This may not consider entities in currently unloaded chunks. Some
	 * implementations may impose artificial restrictions on the maximum
	 * distance.
	 * <p>
	 * <b>Note:</b> Due to display entities having a zero size hitbox, this method will not detect them.
	 * To detect display entities use {@link #rayTraceEntities(Location, Vector, double, double)} with a positive raySize
	 *
	 * @param start       the start position
	 * @param direction   the ray direction
	 * @param maxDistance the maximum distance
	 * @return the closest ray trace hit result, or <code>null</code> if there
	 * is no hit
	 * @see #rayTraceEntities(Location, Vector, double, double, Predicate)
	 */
	@Override
	public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction,
	                                                 double maxDistance) {
		return null;
	}

	/**
	 * Performs a ray trace that checks for entity collisions.
	 * <p>
	 * This may not consider entities in currently unloaded chunks. Some
	 * implementations may impose artificial restrictions on the maximum
	 * distance.
	 *
	 * @param start       the start position
	 * @param direction   the ray direction
	 * @param maxDistance the maximum distance
	 * @param raySize     entity bounding boxes will be uniformly expanded (or
	 *                    shrunk) by this value before doing collision checks
	 * @return the closest ray trace hit result, or <code>null</code> if there
	 * is no hit
	 * @see #rayTraceEntities(Location, Vector, double, double, Predicate)
	 */
	@Override
	public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction,
	                                                 double maxDistance, double raySize) {
		return null;
	}

	/**
	 * Performs a ray trace that checks for entity collisions.
	 * <p>
	 * This may not consider entities in currently unloaded chunks. Some
	 * implementations may impose artificial restrictions on the maximum
	 * distance.
	 * <p>
	 * <b>Note:</b> Due to display entities having a zero size hitbox, this method will not detect them.
	 * To detect display entities use {@link #rayTraceEntities(Location, Vector, double, double, Predicate)} with a positive raySize
	 *
	 * @param start       the start position
	 * @param direction   the ray direction
	 * @param maxDistance the maximum distance
	 * @param filter      only entities that fulfill this predicate are considered,
	 *                    or <code>null</code> to consider all entities
	 * @return the closest ray trace hit result, or <code>null</code> if there
	 * is no hit
	 * @see #rayTraceEntities(Location, Vector, double, double, Predicate)
	 */
	@Override
	public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction,
	                                                 double maxDistance, @Nullable Predicate<? super Entity> filter) {
		return null;
	}

	/**
	 * Performs a ray trace that checks for entity collisions.
	 * <p>
	 * This may not consider entities in currently unloaded chunks. Some
	 * implementations may impose artificial restrictions on the maximum
	 * distance.
	 *
	 * @param start       the start position
	 * @param direction   the ray direction
	 * @param maxDistance the maximum distance
	 * @param raySize     entity bounding boxes will be uniformly expanded (or
	 *                    shrunk) by this value before doing collision checks
	 * @param filter      only entities that fulfill this predicate are considered,
	 *                    or <code>null</code> to consider all entities
	 * @return the closest ray trace hit result, or <code>null</code> if there
	 * is no hit
	 */
	@Override
	public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction,
	                                                 double maxDistance, double raySize,
	                                                 @Nullable Predicate<? super Entity> filter) {
		return null;
	}

	/**
	 * Performs a ray trace that checks for block collisions using the blocks'
	 * precise collision shapes.
	 * <p>
	 * This takes collisions with passable blocks into account, but ignores
	 * fluids.
	 * <p>
	 * This may cause loading of chunks! Some implementations may impose
	 * artificial restrictions on the maximum distance.
	 *
	 * @param start       the start location
	 * @param direction   the ray direction
	 * @param maxDistance the maximum distance
	 * @return the ray trace hit result, or <code>null</code> if there is no hit
	 * @see #rayTraceBlocks(Location, Vector, double, FluidCollisionMode, boolean)
	 */
	@Override
	public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction,
	                                               double maxDistance) {
		return null;
	}

	/**
	 * Performs a ray trace that checks for block collisions using the blocks'
	 * precise collision shapes.
	 * <p>
	 * This takes collisions with passable blocks into account.
	 * <p>
	 * This may cause loading of chunks! Some implementations may impose
	 * artificial restrictions on the maximum distance.
	 *
	 * @param start              the start location
	 * @param direction          the ray direction
	 * @param maxDistance        the maximum distance
	 * @param fluidCollisionMode the fluid collision mode
	 * @return the ray trace hit result, or <code>null</code> if there is no hit
	 * @see #rayTraceBlocks(Location, Vector, double, FluidCollisionMode, boolean)
	 */
	@Override
	public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction,
	                                               double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
		return null;
	}

	/**
	 * Performs a ray trace that checks for block collisions using the blocks'
	 * precise collision shapes.
	 * <p>
	 * If collisions with passable blocks are ignored, fluid collisions are
	 * ignored as well regardless of the fluid collision mode.
	 * <p>
	 * Portal blocks are only considered passable if the ray starts within
	 * them. Apart from that collisions with portal blocks will be considered
	 * even if collisions with passable blocks are otherwise ignored.
	 * <p>
	 * This may cause loading of chunks! Some implementations may impose
	 * artificial restrictions on the maximum distance.
	 *
	 * @param start                the start location
	 * @param direction            the ray direction
	 * @param maxDistance          the maximum distance
	 * @param fluidCollisionMode   the fluid collision mode
	 * @param ignorePassableBlocks whether to ignore passable but collidable
	 *                             blocks (ex. tall grass, signs, fluids, ..)
	 * @return the ray trace hit result, or <code>null</code> if there is no hit
	 */
	@Override
	public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction,
	                                               double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode,
	                                               boolean ignorePassableBlocks) {
		return null;
	}

	/**
	 * Performs a ray trace that checks for both block and entity collisions.
	 * <p>
	 * Block collisions use the blocks' precise collision shapes. The
	 * <code>raySize</code> parameter is only taken into account for entity
	 * collision checks.
	 * <p>
	 * If collisions with passable blocks are ignored, fluid collisions are
	 * ignored as well regardless of the fluid collision mode.
	 * <p>
	 * Portal blocks are only considered passable if the ray starts within them.
	 * Apart from that collisions with portal blocks will be considered even if
	 * collisions with passable blocks are otherwise ignored.
	 * <p>
	 * This may cause loading of chunks! Some implementations may impose
	 * artificial restrictions on the maximum distance.
	 *
	 * @param start                the start location
	 * @param direction            the ray direction
	 * @param maxDistance          the maximum distance
	 * @param fluidCollisionMode   the fluid collision mode
	 * @param ignorePassableBlocks whether to ignore passable but collidable
	 *                             blocks (ex. tall grass, signs, fluids, ..)
	 * @param raySize              entity bounding boxes will be uniformly expanded (or
	 *                             shrunk) by this value before doing collision checks
	 * @param filter               only entities that fulfill this predicate are considered,
	 *                             or <code>null</code> to consider all entities
	 * @return the closest ray trace hit result with either a block or an
	 * entity, or <code>null</code> if there is no hit
	 */
	@Override
	public @Nullable RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction,
	                                         double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode,
	                                         boolean ignorePassableBlocks, double raySize,
	                                         @Nullable Predicate<? super Entity> filter) {
		return null;
	}

	/**
	 * Gets the default spawn {@link Location} of this world
	 *
	 * @return The spawn location of this world
	 */
	@Override
	public @NotNull Location getSpawnLocation() {
		return this.spawnLocation;
	}

	/**
	 * Sets the spawn location of the world.
	 * <br>
	 * The location provided must be equal to this world.
	 *
	 * @param location The {@link Location} to set the spawn for this world at.
	 * @return True if it was successfully set.
	 */
	@Override
	public boolean setSpawnLocation(@NotNull Location location) {
		if (location.getWorld() == this) {
			this.spawnLocation = location;
			return true;
		}

		return false;
	}

	/**
	 * Sets the spawn location of the world
	 *
	 * @param x     X coordinate
	 * @param y     Y coordinate
	 * @param z     Z coordinate
	 * @param angle the angle
	 * @return True if it was successfully set.
	 */
	@Override
	public boolean setSpawnLocation(int x, int y, int z, float angle) {
		this.spawnLocation = new Location(this, x, y, z, angle, 0);
		return true;
	}

	/**
	 * Sets the spawn location of the world
	 *
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * @return True if it was successfully set.
	 */
	@Override
	public boolean setSpawnLocation(int x, int y, int z) {
		this.spawnLocation = new Location(this, x, y, z, 0, 0);
		return true;
	}

	/**
	 * Gets the relative in-game time of this world.
	 * <p>
	 * The relative time is analogous to hours * 1000
	 *
	 * @return The current relative time
	 * @see #getFullTime() Returns an absolute time of this world
	 */
	@Override
	public long getTime() {
		return 0;
	}

	/**
	 * Sets the relative in-game time on the server.
	 * <p>
	 * The relative time is analogous to hours * 1000
	 * <p>
	 * Note that setting the relative time below the current relative time
	 * will actually move the clock forward a day. If you require to rewind
	 * time, please see {@link #setFullTime(long)}
	 *
	 * @param time The new relative time to set the in-game time to (in
	 *             hours*1000)
	 * @see #setFullTime(long) Sets the absolute time of this world
	 */
	@Override
	public void setTime(long time) {

	}

	/**
	 * Gets the full in-game time on this world
	 *
	 * @return The current absolute time
	 * @see #getTime() Returns a relative time of this world
	 */
	@Override
	public long getFullTime() {
		return 0;
	}

	/**
	 * Sets the in-game time on the server
	 * <p>
	 * Note that this sets the full time of the world, which may cause adverse
	 * effects such as breaking redstone clocks and any scheduled events
	 *
	 * @param time The new absolute time to set this world to
	 * @see #setTime(long) Sets the relative time of this world
	 */
	@Override
	public void setFullTime(long time) {

	}

	/**
	 * Gets the full in-game time on this world since the world generation
	 *
	 * @return The current absolute time since the world generation
	 * @see #getTime() Returns a relative time of this world
	 * @see #getFullTime() Returns an absolute time of this world
	 */
	@Override
	public long getGameTime() {
		return 0;
	}

	/**
	 * Returns whether the world has an ongoing storm.
	 *
	 * @return Whether there is an ongoing storm
	 */
	@Override
	public boolean hasStorm() {
		return false;
	}

	/**
	 * Set whether there is a storm. A duration will be set for the new
	 * current conditions.
	 * <p>
	 * This will implicitly call {@link #setClearWeatherDuration(int)} with 0
	 * ticks to reset the world's clear weather.
	 *
	 * @param hasStorm Whether there is rain and snow
	 */
	@Override
	public void setStorm(boolean hasStorm) {

	}

	/**
	 * Get the remaining time in ticks of the current conditions.
	 *
	 * @return Time in ticks
	 */
	@Override
	public int getWeatherDuration() {
		return 0;
	}

	/**
	 * Set the remaining time in ticks of the current conditions.
	 *
	 * @param duration Time in ticks
	 */
	@Override
	public void setWeatherDuration(int duration) {

	}

	/**
	 * Returns whether there is thunder.
	 *
	 * @return Whether there is thunder
	 */
	@Override
	public boolean isThundering() {
		return false;
	}

	/**
	 * Set whether it is thundering.
	 * <p>
	 * This will implicitly call {@link #setClearWeatherDuration(int)} with 0
	 * ticks to reset the world's clear weather.
	 *
	 * @param thundering Whether it is thundering
	 */
	@Override
	public void setThundering(boolean thundering) {

	}

	/**
	 * Get the thundering duration.
	 *
	 * @return Duration in ticks
	 */
	@Override
	public int getThunderDuration() {
		return 0;
	}

	/**
	 * Set the thundering duration.
	 *
	 * @param duration Duration in ticks
	 */
	@Override
	public void setThunderDuration(int duration) {

	}

	/**
	 * Returns whether the world has clear weather.
	 * <p>
	 * This will be true such that {@link #isThundering()} and
	 * {@link #hasStorm()} are both false.
	 *
	 * @return true if clear weather
	 */
	@Override
	public boolean isClearWeather() {
		return false;
	}

	/**
	 * Set the clear weather duration.
	 * <p>
	 * The clear weather ticks determine whether or not the world will be
	 * allowed to rain or storm. If clear weather ticks are &gt; 0, the world will
	 * not naturally do either until the duration has elapsed.
	 * <p>
	 * This method is equivalent to calling {@code /weather clear} with a set
	 * amount of ticks.
	 *
	 * @param duration duration in ticks
	 */
	@Override
	public void setClearWeatherDuration(int duration) {

	}

	/**
	 * Get the clear weather duration.
	 *
	 * @return duration in ticks
	 */
	@Override
	public int getClearWeatherDuration() {
		return 0;
	}

	/**
	 * Creates explosion at given coordinates with given power
	 *
	 * @param x     X coordinate
	 * @param y     Y coordinate
	 * @param z     Z coordinate
	 * @param power The power of explosion, where 4F is TNT
	 * @return false if explosion was canceled, otherwise true
	 */
	@Override
	public boolean createExplosion(double x, double y, double z, float power) {
		return false;
	}

	/**
	 * Creates explosion at given coordinates with given power and optionally
	 * setting blocks on fire.
	 *
	 * @param x       X coordinate
	 * @param y       Y coordinate
	 * @param z       Z coordinate
	 * @param power   The power of explosion, where 4F is TNT
	 * @param setFire Whether or not to set blocks on fire
	 * @return false if explosion was canceled, otherwise true
	 */
	@Override
	public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
		return false;
	}

	/**
	 * Creates explosion at given coordinates with given power and optionally
	 * setting blocks on fire or breaking blocks.
	 *
	 * @param x           X coordinate
	 * @param y           Y coordinate
	 * @param z           Z coordinate
	 * @param power       The power of explosion, where 4F is TNT
	 * @param setFire     Whether or not to set blocks on fire
	 * @param breakBlocks Whether or not to have blocks be destroyed
	 * @return false if explosion was canceled, otherwise true
	 */
	@Override
	public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
		return false;
	}

	/**
	 * Creates explosion at given coordinates with given power and optionally
	 * setting blocks on fire or breaking blocks.
	 * <p>
	 * Note that if a non-null {@code source} Entity is provided and {@code
	 * breakBlocks} is {@code true}, the value of {@code breakBlocks} will be
	 * ignored if {@link GameRule#MOB_GRIEFING} is {@code false} in the world
	 * in which the explosion occurs. In other words, the mob griefing gamerule
	 * will take priority over {@code breakBlocks} if explosions are not allowed.
	 *
	 * @param x           X coordinate
	 * @param y           Y coordinate
	 * @param z           Z coordinate
	 * @param power       The power of explosion, where 4F is TNT
	 * @param setFire     Whether or not to set blocks on fire
	 * @param breakBlocks Whether or not to have blocks be destroyed
	 * @param source      the source entity, used for tracking damage
	 * @return false if explosion was canceled, otherwise true
	 */
	@Override
	public boolean createExplosion(double x, double y, double z, float power, boolean setFire,
	                               boolean breakBlocks, @Nullable Entity source) {
		return false;
	}

	/**
	 * Creates explosion at given coordinates with given power
	 *
	 * @param loc   Location to blow up
	 * @param power The power of explosion, where 4F is TNT
	 * @return false if explosion was canceled, otherwise true
	 */
	@Override
	public boolean createExplosion(@NotNull Location loc, float power) {
		return false;
	}

	/**
	 * Creates explosion at given coordinates with given power and optionally
	 * setting blocks on fire.
	 *
	 * @param loc     Location to blow up
	 * @param power   The power of explosion, where 4F is TNT
	 * @param setFire Whether or not to set blocks on fire
	 * @return false if explosion was canceled, otherwise true
	 */
	@Override
	public boolean createExplosion(@NotNull Location loc, float power, boolean setFire) {
		return false;
	}

	/**
	 * Creates explosion at given coordinates with given power and optionally
	 * setting blocks on fire or breaking blocks.
	 *
	 * @param loc         Location to blow up
	 * @param power       The power of explosion, where 4F is TNT
	 * @param setFire     Whether or not to set blocks on fire
	 * @param breakBlocks Whether or not to have blocks be destroyed
	 * @return false if explosion was canceled, otherwise true
	 */
	@Override
	public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks) {
		return false;
	}

	/**
	 * Creates explosion at given coordinates with given power and optionally
	 * setting blocks on fire or breaking blocks.
	 * <p>
	 * Note that if a non-null {@code source} Entity is provided and {@code
	 * breakBlocks} is {@code true}, the value of {@code breakBlocks} will be
	 * ignored if {@link GameRule#MOB_GRIEFING} is {@code false} in the world
	 * in which the explosion occurs. In other words, the mob griefing gamerule
	 * will take priority over {@code breakBlocks} if explosions are not allowed.
	 *
	 * @param loc         Location to blow up
	 * @param power       The power of explosion, where 4F is TNT
	 * @param setFire     Whether or not to set blocks on fire
	 * @param breakBlocks Whether or not to have blocks be destroyed
	 * @param source      the source entity, used for tracking damage
	 * @return false if explosion was canceled, otherwise true
	 */
	@Override
	public boolean createExplosion(@NotNull Location loc, float power, boolean setFire,
	                               boolean breakBlocks, @Nullable Entity source) {
		return false;
	}

	/**
	 * Gets the current PVP setting for this world.
	 *
	 * @return True if PVP is enabled
	 */
	@Override
	public boolean getPVP() {
		return false;
	}

	/**
	 * Sets the PVP setting for this world.
	 *
	 * @param pvp True/False whether PVP should be Enabled.
	 */
	@Override
	public void setPVP(boolean pvp) {

	}

	/**
	 * Gets the chunk generator for this world
	 *
	 * @return ChunkGenerator associated with this world
	 */
	@Override
	public @Nullable ChunkGenerator getGenerator() {
		return null;
	}

	/**
	 * Gets the biome provider for this world
	 *
	 * @return BiomeProvider associated with this world
	 */
	@Override
	public @Nullable BiomeProvider getBiomeProvider() {
		return null;
	}

	/**
	 * Saves world to disk
	 */
	@Override
	public void save() {

	}

	/**
	 * Gets a list of all applied {@link BlockPopulator}s for this World
	 *
	 * @return List containing any or none BlockPopulators
	 */
	@Override
	public @NotNull List<BlockPopulator> getPopulators() {
		return null;
	}

	/**
	 * Creates a new entity at the given {@link Location} with the supplied
	 * function run before the entity is added to the world.
	 * <br>
	 * Note that when the function is run, the entity will not be actually in
	 * the world. Any operation involving such as teleporting the entity is undefined
	 * until after this function returns.
	 * The passed function however is run after the potential entity's spawn
	 * randomization and hence already allows access to the values of the mob,
	 * whether or not those were randomized, such as attributes or the entity
	 * equipment.
	 *
	 * @param location      the location at which the entity will be spawned.
	 * @param clazz         the class of the {@link LivingEntity} that is to be spawned.
	 * @param spawnReason   the reason provided during the {@link CreatureSpawnEvent} call.
	 * @param randomizeData whether or not the entity's data should be randomised
	 *                      before spawning. By default entities are randomised
	 *                      before spawning in regards to their equipment, age,
	 *                      attributes, etc.
	 *                      An example of this randomization would be the color of
	 *                      a sheep, random enchantments on the equipment of mobs
	 *                      or even a zombie becoming a chicken jockey.
	 *                      If set to false, the entity will not be randomised
	 *                      before spawning, meaning all their data will remain
	 *                      in their default state and not further modifications
	 *                      to the entity will be made.
	 *                      Notably only entities that extend the
	 *                      {@link Mob} interface provide
	 *                      randomisation logic for their spawn.
	 *                      This parameter is hence useless for any other type
	 *                      of entity.
	 * @param function      the function to be run before the entity is spawned.
	 * @return the spawned entity instance.
	 * @throws IllegalArgumentException if either the world or clazz parameter are null.
	 */
	@Override
	public <T extends LivingEntity> @NotNull T spawn(@NotNull Location location,
	                                                 @NotNull Class<T> clazz,
	                                                 CreatureSpawnEvent.@NotNull SpawnReason spawnReason,
	                                                 boolean randomizeData, @Nullable Consumer<? super T> function) throws IllegalArgumentException {
		return null;
	}

	/**
	 * Spawn a {@link FallingBlock} entity at the given {@link Location} of
	 * the specified {@link MaterialData}. The MaterialData dictates what is falling.
	 * When the FallingBlock hits the ground, it will place that block.
	 * <p>
	 * The Material must be a block type, check with {@link Material#isBlock()
	 * data.getItemType().isBlock()}. The Material may not be air.
	 *
	 * @param location The {@link Location} to spawn the FallingBlock
	 * @param data     The block data
	 * @return The spawned {@link FallingBlock} instance
	 * @throws IllegalArgumentException if {@link Location} or {@link
	 *                                  MaterialData} are null or {@link Material} of the {@link MaterialData} is not a block
	 */
	@Override
	public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location,
	                                               @NotNull MaterialData data) throws IllegalArgumentException {
		return null;
	}

	/**
	 * Spawn a {@link FallingBlock} entity at the given {@link Location} of
	 * the specified {@link BlockData}. The BlockData dictates what is falling.
	 * When the FallingBlock hits the ground, it will place that block.
	 *
	 * @param location The {@link Location} to spawn the FallingBlock
	 * @param data     The {@link BlockData} of the FallingBlock to spawn
	 * @return The spawned {@link FallingBlock} instance
	 * @throws IllegalArgumentException if {@link Location} or {@link
	 *                                  BlockData} are null
	 */
	@Override
	public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location,
	                                               @NotNull BlockData data) throws IllegalArgumentException {
		return null;
	}

	/**
	 * Spawn a {@link FallingBlock} entity at the given {@link Location} of the
	 * specified {@link Material}. The material dictates what is falling.
	 * When the FallingBlock hits the ground, it will place that block.
	 * <p>
	 * The Material must be a block type, check with {@link Material#isBlock()
	 * material.isBlock()}. The Material may not be air.
	 *
	 * @param location The {@link Location} to spawn the FallingBlock
	 * @param material The block {@link Material} type
	 * @param data     The block data
	 * @return The spawned {@link FallingBlock} instance
	 * @throws IllegalArgumentException if {@link Location} or {@link
	 *                                  Material} are null or {@link Material} is not a block
	 * @deprecated Magic value
	 */
	@Override
	public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull Material material,
	                                               byte data) throws IllegalArgumentException {
		return null;
	}

	/**
	 * Plays an effect to all players within a default radius around a given
	 * location.
	 *
	 * @param location the {@link Location} around which players must be to
	 *                 hear the sound
	 * @param effect   the {@link Effect}
	 * @param data     a data bit needed for some effects
	 */
	@Override
	public void playEffect(@NotNull Location location, @NotNull Effect effect, int data) {

	}

	/**
	 * Plays an effect to all players within a given radius around a location.
	 *
	 * @param location the {@link Location} around which players must be to
	 *                 hear the effect
	 * @param effect   the {@link Effect}
	 * @param data     a data bit needed for some effects
	 * @param radius   the radius around the location
	 */
	@Override
	public void playEffect(@NotNull Location location, @NotNull Effect effect, int data, int radius) {

	}

	/**
	 * Plays an effect to all players within a default radius around a given
	 * location.
	 *
	 * @param location the {@link Location} around which players must be to
	 *                 hear the sound
	 * @param effect   the {@link Effect}
	 * @param data     a data bit needed for some effects
	 */
	@Override
	public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data) {

	}

	/**
	 * Plays an effect to all players within a given radius around a location.
	 *
	 * @param location the {@link Location} around which players must be to
	 *                 hear the effect
	 * @param effect   the {@link Effect}
	 * @param data     a data bit needed for some effects
	 * @param radius   the radius around the location
	 */
	@Override
	public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data, int radius) {

	}

	/**
	 * Get empty chunk snapshot (equivalent to all air blocks), optionally
	 * including valid biome data. Used for representing an ungenerated chunk,
	 * or for fetching only biome data without loading a chunk.
	 *
	 * @param x                - chunk x coordinate
	 * @param z                - chunk z coordinate
	 * @param includeBiome     - if true, snapshot includes per-coordinate biome
	 *                         type
	 * @param includeBiomeTemp - if true, snapshot includes per-coordinate
	 *                         raw biome temperature
	 * @return The empty snapshot.
	 */
	@Override
	public @NotNull ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {
		return null;
	}

	/**
	 * Sets the spawn flags for this.
	 *
	 * @param allowMonsters - if true, monsters are allowed to spawn in this
	 *                      world.
	 * @param allowAnimals  - if true, animals are allowed to spawn in this
	 *                      world.
	 */
	@Override
	public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {

	}

	/**
	 * Gets whether animals can spawn in this world.
	 *
	 * @return whether animals can spawn in this world.
	 */
	@Override
	public boolean getAllowAnimals() {
		return false;
	}

	/**
	 * Gets whether monsters can spawn in this world.
	 *
	 * @return whether monsters can spawn in this world.
	 */
	@Override
	public boolean getAllowMonsters() {
		return false;
	}

	/**
	 * Gets the biome for the given block coordinates.
	 *
	 * @param x X coordinate of the block
	 * @param z Z coordinate of the block
	 * @return Biome of the requested block
	 * @deprecated biomes are now 3-dimensional
	 */
	@Override
	public @NotNull Biome getBiome(int x, int z) {
		return null;
	}

	/**
	 * Sets the biome for the given block coordinates
	 *
	 * @param x   X coordinate of the block
	 * @param z   Z coordinate of the block
	 * @param bio new Biome type for this block
	 * @deprecated biomes are now 3-dimensional
	 */
	@Override
	public void setBiome(int x, int z, @NotNull Biome bio) {

	}

	/**
	 * Gets the temperature for the given block coordinates.
	 * <p>
	 * It is safe to run this method when the block does not exist, it will
	 * not create the block.
	 * <p>
	 * This method will return the raw temperature without adjusting for block
	 * height effects.
	 *
	 * @param x X coordinate of the block
	 * @param z Z coordinate of the block
	 * @return Temperature of the requested block
	 * @deprecated biomes are now 3-dimensional
	 */
	@Override
	public double getTemperature(int x, int z) {
		return 0;
	}

	/**
	 * Gets the temperature for the given block coordinates.
	 * <p>
	 * It is safe to run this method when the block does not exist, it will
	 * not create the block.
	 * <p>
	 * This method will return the raw temperature without adjusting for block
	 * height effects.
	 *
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 * @param z Z coordinate of the block
	 * @return Temperature of the requested block
	 */
	@Override
	public double getTemperature(int x, int y, int z) {
		return 0;
	}

	/**
	 * Gets the humidity for the given block coordinates.
	 * <p>
	 * It is safe to run this method when the block does not exist, it will
	 * not create the block.
	 *
	 * @param x X coordinate of the block
	 * @param z Z coordinate of the block
	 * @return Humidity of the requested block
	 * @deprecated biomes are now 3-dimensional
	 */
	@Override
	public double getHumidity(int x, int z) {
		return 0;
	}

	/**
	 * Gets the humidity for the given block coordinates.
	 * <p>
	 * It is safe to run this method when the block does not exist, it will
	 * not create the block.
	 *
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 * @param z Z coordinate of the block
	 * @return Humidity of the requested block
	 */
	@Override
	public double getHumidity(int x, int y, int z) {
		return 0;
	}

	/**
	 * Gets the maximum height to which chorus fruits and nether portals can
	 * bring players within this dimension.
	 * <p>
	 * This excludes portals that were already built above the limit as they
	 * still connect normally. May not be greater than {@link #getMaxHeight()}.
	 *
	 * @return maximum logical height for chorus fruits and nether portals
	 */
	@Override
	public int getLogicalHeight() {
		return 0;
	}

	/**
	 * Gets if this world is natural.
	 * <p>
	 * When false, compasses spin randomly, and using a bed to set the respawn
	 * point or sleep, is disabled. When true, nether portals can spawn
	 * zombified piglins.
	 *
	 * @return true if world is natural
	 */
	@Override
	public boolean isNatural() {
		return false;
	}

	/**
	 * Gets if beds work in this world.
	 * <p>
	 * A non-working bed will blow up when trying to sleep. {@link #isNatural()}
	 * defines if a bed can be used to set spawn point.
	 *
	 * @return true if beds work in this world
	 */
	@Override
	public boolean isBedWorks() {
		return false;
	}

	/**
	 * Gets if this world has skylight access.
	 *
	 * @return true if this world has skylight access
	 */
	@Override
	public boolean hasSkyLight() {
		return false;
	}

	/**
	 * Gets if this world has a ceiling.
	 *
	 * @return true if this world has a bedrock ceiling
	 */
	@Override
	public boolean hasCeiling() {
		return false;
	}

	/**
	 * Gets if this world allow to piglins to survive without shaking and
	 * transforming to zombified piglins.
	 *
	 * @return true if piglins will not transform to zombified piglins
	 */
	@Override
	public boolean isPiglinSafe() {
		return false;
	}

	/**
	 * Gets if this world allows players to charge and use respawn anchors.
	 *
	 * @return true if players can charge and use respawn anchors
	 */
	@Override
	public boolean isRespawnAnchorWorks() {
		return false;
	}

	/**
	 * Gets if players with the bad omen effect in this world will trigger a
	 * raid.
	 *
	 * @return true if raids will be triggered
	 */
	@Override
	public boolean hasRaids() {
		return false;
	}

	/**
	 * Gets if various water/lava mechanics will be triggered in this world, eg:
	 * <br>
	 * <ul>
	 * <li>Water is evaporated</li>
	 * <li>Sponges dry</li>
	 * <li>Lava spreads faster and further</li>
	 * </ul>
	 *
	 * @return true if this world has the above mechanics
	 */
	@Override
	public boolean isUltraWarm() {
		return false;
	}

	/**
	 * Gets the sea level for this world.
	 * <p>
	 * This is often half of {@link #getMaxHeight()}
	 *
	 * @return Sea level
	 */
	@Override
	public int getSeaLevel() {
		return 0;
	}

	/**
	 * Gets whether the world's spawn area should be kept loaded into memory
	 * or not.
	 *
	 * @return true if the world's spawn area will be kept loaded into memory.
	 * @deprecated use {@link GameRule#SPAWN_CHUNK_RADIUS} for finer control
	 */
	@Override
	public boolean getKeepSpawnInMemory() {
		return false;
	}

	/**
	 * Sets whether the world's spawn area should be kept loaded into memory
	 * or not.
	 *
	 * @param keepLoaded if true then the world's spawn area will be kept
	 *                   loaded into memory.
	 * @deprecated use {@link GameRule#SPAWN_CHUNK_RADIUS} for finer control
	 */
	@Override
	public void setKeepSpawnInMemory(boolean keepLoaded) {

	}

	/**
	 * Gets whether or not the world will automatically save
	 *
	 * @return true if the world will automatically save, otherwise false
	 */
	@Override
	public boolean isAutoSave() {
		return false;
	}

	/**
	 * Sets whether or not the world will automatically save
	 *
	 * @param value true if the world should automatically save, otherwise
	 *              false
	 */
	@Override
	public void setAutoSave(boolean value) {

	}

	/**
	 * Sets the Difficulty of the world.
	 *
	 * @param difficulty the new difficulty you want to set the world to
	 */
	@Override
	public void setDifficulty(@NotNull Difficulty difficulty) {

	}

	/**
	 * Gets the Difficulty of the world.
	 *
	 * @return The difficulty of the world.
	 */
	@Override
	public @NotNull Difficulty getDifficulty() {
		return null;
	}

	/**
	 * Returns the view distance used for this world.
	 *
	 * @return the view distance used for this world
	 */
	@Override
	public int getViewDistance() {
		return 0;
	}

	/**
	 * Returns the simulation distance used for this world.
	 *
	 * @return the simulation distance used for this world
	 */
	@Override
	public int getSimulationDistance() {
		return 0;
	}

	/**
	 * Gets the folder of this world on disk.
	 *
	 * @return The folder of this world.
	 */
	@Override
	public @NotNull File getWorldFolder() {
		return null;
	}

	/**
	 * Gets the type of this world.
	 *
	 * @return Type of this world.
	 * @deprecated world type is only used to select the default word generation
	 * settings and is not stored in Vanilla worlds, making it impossible for
	 * this method to always return the correct value.
	 */
	@Override
	public @Nullable WorldType getWorldType() {
		return null;
	}

	/**
	 * Gets whether or not structures are being generated.
	 *
	 * @return True if structures are being generated.
	 */
	@Override
	public boolean canGenerateStructures() {
		return false;
	}

	/**
	 * Gets whether the world is hardcore or not.
	 * <p>
	 * In a hardcore world the difficulty is locked to hard.
	 *
	 * @return hardcore status
	 */
	@Override
	public boolean isHardcore() {
		return false;
	}

	/**
	 * Sets whether the world is hardcore or not.
	 * <p>
	 * In a hardcore world the difficulty is locked to hard.
	 *
	 * @param hardcore Whether the world is hardcore
	 */
	@Override
	public void setHardcore(boolean hardcore) {

	}

	/**
	 * Gets the world's ticks per animal spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn animals.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn animals in
	 *     this world every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn animals
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, animal spawning will be disabled for this world. We
	 * recommend using {@link #setSpawnFlags(boolean, boolean)} to control
	 * this instead.
	 * <p>
	 * Minecraft default: 400.
	 *
	 * @return The world's ticks per animal spawns value
	 * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
	 */
	@Override
	public long getTicksPerAnimalSpawns() {
		return 0;
	}

	/**
	 * Sets the world's ticks per animal spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn animals.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn animals in
	 *     this world every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn animals
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, animal spawning will be disabled for this world. We
	 * recommend using {@link #setSpawnFlags(boolean, boolean)} to control
	 * this instead.
	 * <p>
	 * Minecraft default: 400.
	 *
	 * @param ticksPerAnimalSpawns the ticks per animal spawns value you want
	 *                             to set the world to
	 * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
	 */
	@Override
	public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {

	}

	/**
	 * Gets the world's ticks per monster spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn monsters.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn monsters in
	 *     this world every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn monsters
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, monsters spawning will be disabled for this world. We
	 * recommend using {@link #setSpawnFlags(boolean, boolean)} to control
	 * this instead.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @return The world's ticks per monster spawns value
	 * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
	 */
	@Override
	public long getTicksPerMonsterSpawns() {
		return 0;
	}

	/**
	 * Sets the world's ticks per monster spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn monsters.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn monsters in
	 *     this world on every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn monsters
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, monsters spawning will be disabled for this world. We
	 * recommend using {@link #setSpawnFlags(boolean, boolean)} to control
	 * this instead.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @param ticksPerMonsterSpawns the ticks per monster spawns value you
	 *                              want to set the world to
	 * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
	 */
	@Override
	public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {

	}

	/**
	 * Gets the world's ticks per water mob spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn water mobs.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn water mobs in
	 *     this world every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn water mobs
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, water mobs spawning will be disabled for this world.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @return The world's ticks per water mob spawns value
	 * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
	 */
	@Override
	public long getTicksPerWaterSpawns() {
		return 0;
	}

	/**
	 * Sets the world's ticks per water mob spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn water mobs.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn water mobs in
	 *     this world on every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn water mobs
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, water mobs spawning will be disabled for this world.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @param ticksPerWaterSpawns the ticks per water mob spawns value you
	 *                            want to set the world to
	 * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
	 */
	@Override
	public void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {

	}

	/**
	 * Gets the default ticks per water ambient mob spawns value.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn water ambient mobs
	 *     every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn water ambient mobs
	 *     every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b> If set to 0, ambient mobs spawning will be disabled.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @return the default ticks per water ambient mobs spawn value
	 * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
	 */
	@Override
	public long getTicksPerWaterAmbientSpawns() {
		return 0;
	}

	/**
	 * Sets the world's ticks per water ambient mob spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn water ambient mobs.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn water ambient mobs in
	 *     this world on every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn water ambient mobs
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, water ambient mobs spawning will be disabled for this world.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @param ticksPerAmbientSpawns the ticks per water ambient mob spawns value you
	 *                              want to set the world to
	 * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
	 */
	@Override
	public void setTicksPerWaterAmbientSpawns(int ticksPerAmbientSpawns) {

	}

	/**
	 * Gets the default ticks per water underground creature spawns value.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn water underground creature
	 *     every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn water underground creature
	 *     every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b> If set to 0, water underground creature spawning will be disabled.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @return the default ticks per water underground creature spawn value
	 * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
	 */
	@Override
	public long getTicksPerWaterUndergroundCreatureSpawns() {
		return 0;
	}

	/**
	 * Sets the world's ticks per water underground creature spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn water underground creature.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn water underground creature in
	 *     this world on every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn water underground creature
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, water underground creature spawning will be disabled for this world.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @param ticksPerWaterUndergroundCreatureSpawns the ticks per water underground creature spawns value you
	 *                                               want to set the world to
	 * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
	 */
	@Override
	public void setTicksPerWaterUndergroundCreatureSpawns(int ticksPerWaterUndergroundCreatureSpawns) {

	}

	/**
	 * Gets the world's ticks per ambient mob spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn ambient mobs.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn ambient mobs in
	 *     this world every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn ambient mobs
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, ambient mobs spawning will be disabled for this world.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @return the default ticks per ambient mobs spawn value
	 * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
	 */
	@Override
	public long getTicksPerAmbientSpawns() {
		return 0;
	}

	/**
	 * Sets the world's ticks per ambient mob spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn ambient mobs.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn ambient mobs in
	 *     this world on every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn ambient mobs
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, ambient mobs spawning will be disabled for this world.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @param ticksPerAmbientSpawns the ticks per ambient mob spawns value you
	 *                              want to set the world to
	 * @deprecated Deprecated in favor of {@link #setTicksPerSpawns(SpawnCategory, int)}
	 */
	@Override
	public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {

	}

	/**
	 * Gets the world's ticks per {@link SpawnCategory} mob spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn {@link SpawnCategory} mobs.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn {@link SpawnCategory} mobs in
	 *     this world every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn {@link SpawnCategory} mobs
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, {@link SpawnCategory} mobs spawning will be disabled for this world.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @param spawnCategory the category spawn
	 * @return The world's ticks per {@link SpawnCategory} mob spawns value
	 */
	@Override
	public long getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
		return 0;
	}

	/**
	 * Sets the world's ticks per {@link SpawnCategory} mob spawns value
	 * <p>
	 * This value determines how many ticks there are between attempts to
	 * spawn {@link SpawnCategory} mobs.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn {@link SpawnCategory} mobs in
	 *     this world on every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn {@link SpawnCategory} mobs
	 *     in this world every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b>
	 * If set to 0, {@link SpawnCategory} mobs spawning will be disabled for this world.
	 * <p>
	 * Minecraft default: 1.
	 *  @param spawnCategory the category spawn
	 *
	 * @param ticksPerCategorySpawn the ticks per {@link SpawnCategory} mob spawns value you
	 *                              want to set the world to
	 */
	@Override
	public void setTicksPerSpawns(@NotNull SpawnCategory spawnCategory, int ticksPerCategorySpawn) {

	}

	/**
	 * Gets limit for number of monsters that can spawn in a chunk in this
	 * world
	 *
	 * @return The monster spawn limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override
	public int getMonsterSpawnLimit() {
		return 0;
	}

	/**
	 * Sets the limit for number of monsters that can spawn in a chunk in this
	 * world
	 * <p>
	 * <b>Note:</b> If set to a negative number the world will use the
	 * server-wide spawn limit instead.
	 *
	 * @param limit the new mob limit
	 * @deprecated Deprecated in favor of {@link #setSpawnLimit(SpawnCategory, int)}
	 */
	@Override
	public void setMonsterSpawnLimit(int limit) {

	}

	/**
	 * Gets the limit for number of animals that can spawn in a chunk in this
	 * world
	 *
	 * @return The animal spawn limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override
	public int getAnimalSpawnLimit() {
		return 0;
	}

	/**
	 * Sets the limit for number of animals that can spawn in a chunk in this
	 * world
	 * <p>
	 * <b>Note:</b> If set to a negative number the world will use the
	 * server-wide spawn limit instead.
	 *
	 * @param limit the new mob limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override
	public void setAnimalSpawnLimit(int limit) {

	}

	/**
	 * Gets the limit for number of water animals that can spawn in a chunk in
	 * this world
	 *
	 * @return The water animal spawn limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override
	public int getWaterAnimalSpawnLimit() {
		return 0;
	}

	/**
	 * Sets the limit for number of water animals that can spawn in a chunk in
	 * this world
	 * <p>
	 * <b>Note:</b> If set to a negative number the world will use the
	 * server-wide spawn limit instead.
	 *
	 * @param limit the new mob limit
	 * @deprecated Deprecated in favor of {@link #setSpawnLimit(SpawnCategory, int)}
	 */
	@Override
	public void setWaterAnimalSpawnLimit(int limit) {

	}

	/**
	 * Gets the limit for number of water underground creature that can spawn in a chunk in
	 * this world
	 *
	 * @return The water underground creature spawn limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override
	public int getWaterUndergroundCreatureSpawnLimit() {
		return 0;
	}

	/**
	 * Sets the limit for number of water underground creature that can spawn in a chunk in
	 * this world
	 * <p>
	 * <b>Note:</b> If set to a negative number the world will use the
	 * server-wide spawn limit instead.
	 *
	 * @param limit the new mob limit
	 * @deprecated Deprecated in favor of {@link #setSpawnLimit(SpawnCategory, int)}
	 */
	@Override
	public void setWaterUndergroundCreatureSpawnLimit(int limit) {

	}

	/**
	 * Gets user-specified limit for number of water ambient mobs that can spawn
	 * in a chunk.
	 *
	 * @return the water ambient spawn limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override
	public int getWaterAmbientSpawnLimit() {
		return 0;
	}

	/**
	 * Sets the limit for number of water ambient mobs that can spawn in a chunk
	 * in this world
	 * <p>
	 * <b>Note:</b> If set to a negative number the world will use the
	 * server-wide spawn limit instead.
	 *
	 * @param limit the new mob limit
	 * @deprecated Deprecated in favor of {@link #setSpawnLimit(SpawnCategory, int)}
	 */
	@Override
	public void setWaterAmbientSpawnLimit(int limit) {

	}

	/**
	 * Gets the limit for number of ambient mobs that can spawn in a chunk in
	 * this world
	 *
	 * @return The ambient spawn limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override
	public int getAmbientSpawnLimit() {
		return 0;
	}

	/**
	 * Sets the limit for number of ambient mobs that can spawn in a chunk in
	 * this world
	 * <p>
	 * <b>Note:</b> If set to a negative number the world will use the
	 * server-wide spawn limit instead.
	 *
	 * @param limit the new mob limit
	 * @deprecated Deprecated in favor of {@link #setSpawnLimit(SpawnCategory, int)}
	 */
	@Override
	public void setAmbientSpawnLimit(int limit) {

	}

	/**
	 * Gets the limit for number of {@link SpawnCategory} entities that can spawn in a chunk in
	 * this world
	 *
	 * @param spawnCategory the entity category
	 * @return The ambient spawn limit
	 */
	@Override
	public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
		return 0;
	}

	/**
	 * Sets the limit for number of {@link SpawnCategory} entities that can spawn in a chunk in
	 * this world
	 * <p>
	 * <b>Note:</b> If set to a negative number the world will use the
	 * server-wide spawn limit instead.
	 *
	 * @param spawnCategory the entity category
	 * @param limit         the new mob limit
	 */
	@Override
	public void setSpawnLimit(@NotNull SpawnCategory spawnCategory, int limit) {

	}

	/**
	 * Play a note at the provided Location in the World. <br>
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

	}

	/**
	 * Play a Sound at the provided Location in the World.
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

	}

	/**
	 * Play a Sound at the provided Location in the World.
	 * <p>
	 * This function will fail silently if Location or Sound are null. No
	 * sound will be heard by the players if their clients do not have the
	 * respective sound for the value passed.
	 *
	 * @param location The location to play the sound
	 * @param sound    The internal sound name to play
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 */
	@Override
	public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {

	}

	/**
	 * Play a Sound at the provided Location in the World.
	 * <p>
	 * This function will fail silently if Location or Sound are null.
	 *
	 * @param location The location to play the sound
	 * @param sound    The sound to play
	 * @param category the category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 */
	@Override
	public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {

	}

	/**
	 * Play a Sound at the provided Location in the World.
	 * <p>
	 * This function will fail silently if Location or Sound are null. No sound
	 * will be heard by the players if their clients do not have the respective
	 * sound for the value passed.
	 *
	 * @param location The location to play the sound
	 * @param sound    The internal sound name to play
	 * @param category The category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 */
	@Override
	public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {

	}

	/**
	 * Play a Sound at the provided Location in the World. For sounds with multiple
	 * variations passing the same seed will always play the same variation.
	 * <p>
	 * This function will fail silently if Location or Sound are null.
	 *
	 * @param location The location to play the sound
	 * @param sound    The sound to play
	 * @param category the category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 * @param seed     The seed for the sound
	 */
	@Override
	public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch, long seed) {

	}

	/**
	 * Play a Sound at the provided Location in the World. For sounds with multiple
	 * variations passing the same seed will always play the same variation.
	 * <p>
	 * This function will fail silently if Location or Sound are null. No sound will
	 * be heard by the players if their clients do not have the respective sound for
	 * the value passed.
	 *
	 * @param location The location to play the sound
	 * @param sound    The internal sound name to play
	 * @param category the category of the sound
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 * @param seed     The seed for the sound
	 */
	@Override
	public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch, long seed) {

	}

	/**
	 * Play a Sound at the location of the provided entity in the World.
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

	}

	/**
	 * Play a Sound at the location of the provided entity in the World.
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

	}

	/**
	 * Play a Sound at the location of the provided entity in the World.
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
	public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {

	}

	/**
	 * Play a Sound at the location of the provided entity in the World.
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
	public void playSound(@NotNull Entity entity, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {

	}

	/**
	 * Play a Sound at the location of the provided entity in the World. For sounds
	 * with multiple variations passing the same seed will always play the same
	 * variation.
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
	public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch, long seed) {

	}

	/**
	 * Play a Sound at the location of the provided entity in the World. For sounds
	 * with multiple variations passing the same seed will always play the same
	 * variation.
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
	public void playSound(@NotNull Entity entity, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch, long seed) {

	}

	/**
	 * Get an array containing the names of all the {@link GameRule}s.
	 *
	 * @return An array of {@link GameRule} names.
	 */
	@Override
	public @NotNull String[] getGameRules() {
		return new String[0];
	}

	/**
	 * Gets the current state of the specified rule
	 * <p>
	 * Will return null if rule passed is null
	 *
	 * @param rule Rule to look up value of
	 * @return String value of rule
	 * @deprecated use {@link #getGameRuleValue(GameRule)} instead
	 */
	@Override
	public @Nullable String getGameRuleValue(@Nullable String rule) {
		return null;
	}

	/**
	 * Set the specified gamerule to specified value.
	 * <p>
	 * The rule may attempt to validate the value passed, will return true if
	 * value was set.
	 * <p>
	 * If rule is null, the function will return false.
	 *
	 * @param rule  Rule to set
	 * @param value Value to set rule to
	 * @return True if rule was set
	 * @deprecated use {@link #setGameRule(GameRule, Object)} instead.
	 */
	@Override
	public boolean setGameRuleValue(@NotNull String rule, @NotNull String value) {
		return false;
	}

	/**
	 * Checks if string is a valid game rule
	 *
	 * @param rule Rule to check
	 * @return True if rule exists
	 */
	@Override
	public boolean isGameRule(@NotNull String rule) {
		return false;
	}

	/**
	 * Get the current value for a given {@link GameRule}.
	 *
	 * @param rule the GameRule to check
	 * @return the current value
	 */
	@Override
	public <T> @Nullable T getGameRuleValue(@NotNull GameRule<T> rule) {
		return null;
	}

	/**
	 * Get the default value for a given {@link GameRule}. This value is not
	 * guaranteed to match the current value.
	 *
	 * @param rule the rule to return a default value for
	 * @return the default value
	 */
	@Override
	public <T> @Nullable T getGameRuleDefault(@NotNull GameRule<T> rule) {
		return null;
	}

	/**
	 * Set the given {@link GameRule}'s new value.
	 *
	 * @param rule     the GameRule to update
	 * @param newValue the new value
	 * @return true if the value was successfully set
	 */
	@Override
	public <T> boolean setGameRule(@NotNull GameRule<T> rule, @NotNull T newValue) {
		return false;
	}

	/**
	 * Gets the world border for this world.
	 *
	 * @return The world border for this world.
	 */
	@Override
	public @NotNull WorldBorder getWorldBorder() {
		return null;
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
	public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, @Nullable T data) {

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
	public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, @Nullable T data) {

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
	public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ) {

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
	public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {

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
	public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {

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
	public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {

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
	public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {

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
	public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {

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
	public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {

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
	public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {

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
	 * @param force    whether to send the particle to players within an extended
	 *                 range and encourage their client to render it regardless of
	 *                 settings
	 */
	@Override
	public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {

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
	 * @param force    whether to send the particle to players within an extended
	 *                 range and encourage their client to render it regardless of
	 *                 settings
	 */
	@Override
	public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {

	}

	/**
	 * Find the closest nearby structure of a given {@link StructureType}.
	 * Finding unexplored structures can, and will, block if the world is
	 * looking in chunks that gave not generated yet. This can lead to the world
	 * temporarily freezing while locating an unexplored structure.
	 * <p>
	 * The {@code radius} is not a rigid square radius. Each structure may alter
	 * how many chunks to check for each iteration. Do not assume that only a
	 * radius x radius chunk area will be checked. For example,
	 * {@link StructureType#WOODLAND_MANSION} can potentially check up to 20,000
	 * blocks away (or more) regardless of the radius used.
	 * <p>
	 * This will <i>not</i> load or generate chunks. This can also lead to
	 * instances where the server can hang if you are only looking for
	 * unexplored structures. This is because it will keep looking further and
	 * further out in order to find the structure.
	 *
	 * @param origin         where to start looking for a structure
	 * @param structureType  the type of structure to find
	 * @param radius         the radius, in chunks, around which to search
	 * @param findUnexplored true to only find unexplored structures
	 * @return the closest {@link Location}, or null if no structure of the
	 * specified type exists.
	 * @see #locateNearestStructure(Location, Structure, int, boolean)
	 * @see #locateNearestStructure(Location, StructureType, int, boolean)
	 * @deprecated Use
	 * {@link #locateNearestStructure(Location, Structure, int, boolean)} or
	 * {@link #locateNearestStructure(Location, StructureType, int, boolean)}
	 * instead.
	 */
	@Override
	public @Nullable Location locateNearestStructure(@NotNull Location origin, @NotNull StructureType structureType, int radius, boolean findUnexplored) {
		return null;
	}

	/**
	 * Find the closest nearby structure of a given {@link StructureType}.
	 * Finding unexplored structures can, and will, block if the world is
	 * looking in chunks that gave not generated yet. This can lead to the world
	 * temporarily freezing while locating an unexplored structure.
	 * <p>
	 * The {@code radius} is not a rigid square radius. Each structure may alter
	 * how many chunks to check for each iteration. Do not assume that only a
	 * radius x radius chunk area will be checked. For example,
	 * {@link StructureType#WOODLAND_MANSION} can potentially check up to 20,000
	 * blocks away (or more) regardless of the radius used.
	 * <p>
	 * This will <i>not</i> load or generate chunks. This can also lead to
	 * instances where the server can hang if you are only looking for
	 * unexplored structures. This is because it will keep looking further and
	 * further out in order to find the structure.
	 * <p>
	 * The difference between searching for a {@link StructureType} and a
	 * {@link Structure} is, that a {@link StructureType} can refer to multiple
	 * {@link Structure Structures} while searching for a {@link Structure}
	 * while only search for the given {@link Structure}.
	 *
	 * @param origin         where to start looking for a structure
	 * @param structureType  the type of structure to find
	 * @param radius         the radius, in chunks, around which to search
	 * @param findUnexplored true to only find unexplored structures
	 * @return the closest {@link Location} and {@link Structure}, or null if no
	 * structure of the specified type exists.
	 * @see #locateNearestStructure(Location, Structure, int, boolean)
	 */
	@Override
	public @Nullable StructureSearchResult locateNearestStructure(@NotNull Location origin, org.bukkit.generator.structure.@NotNull StructureType structureType, int radius, boolean findUnexplored) {
		return null;
	}

	/**
	 * Find the closest nearby structure of a given {@link Structure}. Finding
	 * unexplored structures can, and will, block if the world is looking in
	 * chunks that gave not generated yet. This can lead to the world
	 * temporarily freezing while locating an unexplored structure.
	 * <p>
	 * The {@code radius} is not a rigid square radius. Each structure may alter
	 * how many chunks to check for each iteration. Do not assume that only a
	 * radius x radius chunk area will be checked. For example,
	 * {@link Structure#MANSION} can potentially check up to 20,000 blocks away
	 * (or more) regardless of the radius used.
	 * <p>
	 * This will <i>not</i> load or generate chunks. This can also lead to
	 * instances where the server can hang if you are only looking for
	 * unexplored structures. This is because it will keep looking further and
	 * further out in order to find the structure.
	 * <p>
	 * The difference between searching for a {@link StructureType} and a
	 * {@link Structure} is, that a {@link StructureType} can refer to multiple
	 * {@link Structure Structures} while searching for a {@link Structure}
	 * while only search for the given {@link Structure}.
	 *
	 * @param origin         where to start looking for a structure
	 * @param structure      the structure to find
	 * @param radius         the radius, in chunks, around which to search
	 * @param findUnexplored true to only find unexplored structures
	 * @return the closest {@link Location} and {@link Structure}, or null if no
	 * structure was found.
	 * @see #locateNearestStructure(Location, StructureType, int, boolean)
	 */
	@Override
	public @Nullable StructureSearchResult locateNearestStructure(@NotNull Location origin, @NotNull Structure structure, int radius, boolean findUnexplored) {
		return null;
	}

	/**
	 * @return 
	 */
	@Override
	public @NotNull Spigot spigot() {
		return null;
	}

	/**
	 * Find the closest nearby location with a biome matching the provided
	 * {@link Biome}(s). Finding biomes can, and will, block if the world is looking
	 * in chunks that have not generated yet. This can lead to the world temporarily
	 * freezing while locating a biome.
	 * <p>
	 * <b>Note:</b> This will <i>not</i> reflect changes made to the world after
	 * generation, this method only sees the biome at the time of world generation.
	 * This will <i>not</i> load or generate chunks.
	 * <p>
	 * If multiple biomes are provided {@link BiomeSearchResult#getBiome()} will
	 * indicate which one was located.
	 * <p>
	 * This method will use a horizontal interval of 32 and a vertical interval of
	 * 64, equal to the /locate command.
	 *
	 * @param origin where to start looking for a biome
	 * @param radius the radius, in blocks, around which to search
	 * @param biomes the biomes to search for
	 * @return a BiomeSearchResult containing the closest {@link Location} and
	 * {@link Biome}, or null if no biome was found.
	 * @see #locateNearestBiome(Location, int, int, int, Biome...)
	 */
	@Override
	public @Nullable BiomeSearchResult locateNearestBiome(@NotNull Location origin, int radius, @NotNull Biome... biomes) {
		return null;
	}

	/**
	 * Find the closest nearby location with a biome matching the provided
	 * {@link Biome}(s). Finding biomes can, and will, block if the world is looking
	 * in chunks that have not generated yet. This can lead to the world temporarily
	 * freezing while locating a biome.
	 * <p>
	 * <b>Note:</b> This will <i>not</i> reflect changes made to the world after
	 * generation, this method only sees the biome at the time of world generation.
	 * This will <i>not</i> load or generate chunks.
	 * <p>
	 * If multiple biomes are provided {@link BiomeSearchResult#getBiome()} will
	 * indicate which one was located. Higher values for {@code horizontalInterval}
	 * and {@code verticalInterval} will result in faster searches, but may lead to
	 * small biomes being missed.
	 *
	 * @param origin             where to start looking for a biome
	 * @param radius             the radius, in blocks, around which to search
	 * @param horizontalInterval the horizontal distance between each check
	 * @param verticalInterval   the vertical distance between each check
	 * @param biomes             the biomes to search for
	 * @return a BiomeSearchResult containing the closest {@link Location} and
	 * {@link Biome}, or null if no biome was found.
	 * @see #locateNearestBiome(Location, int, Biome...)
	 */
	@Override
	public @Nullable BiomeSearchResult locateNearestBiome(@NotNull Location origin, int radius, int horizontalInterval, int verticalInterval, @NotNull Biome... biomes) {
		return null;
	}

	/**
	 * Finds the nearest raid close to the given location.
	 *
	 * @param location the origin location
	 * @param radius   the radius
	 * @return the closest {@link Raid}, or null if no raids were found
	 */
	@Override
	public @Nullable Raid locateNearestRaid(@NotNull Location location, int radius) {
		return null;
	}

	/**
	 * Gets all raids that are going on over this world.
	 *
	 * @return the list of all active raids
	 */
	@Override
	public @NotNull List<Raid> getRaids() {
		return null;
	}

	/**
	 * Get the {@link DragonBattle} associated with this world.
	 * <p>
	 * If this world's environment is not {@link Environment#THE_END}, null will
	 * be returned.
	 * <p>
	 * If an end world, a dragon battle instance will be returned regardless of
	 * whether or not a dragon is present in the world or a fight sequence has
	 * been activated. The dragon battle instance acts as a state holder.
	 *
	 * @return the dragon battle instance
	 */
	@Override
	public @Nullable DragonBattle getEnderDragonBattle() {
		return null;
	}

	/**
	 * Get all {@link FeatureFlag} enabled in this world.
	 *
	 * @return all enabled {@link FeatureFlag}
	 */
	@Override
	public @NotNull Set<FeatureFlag> getFeatureFlags() {
		return null;
	}

	/**
	 * Gets all generated structures that intersect the chunk at the given
	 * coordinates. <br>
	 * If no structures are present an empty collection will be returned.
	 *
	 * @param x X-coordinate of the chunk
	 * @param z Z-coordinate of the chunk
	 * @return a collection of placed structures in the chunk at the given
	 * coordinates
	 */
	@Override
	public @NotNull Collection<GeneratedStructure> getStructures(int x, int z) {
		return null;
	}

	/**
	 * Gets all generated structures of a given {@link Structure} that intersect
	 * the chunk at the given coordinates. <br>
	 * If no structures are present an empty collection will be returned.
	 *
	 * @param x         X-coordinate of the chunk
	 * @param z         Z-coordinate of the chunk
	 * @param structure the structure to find
	 * @return a collection of placed structures in the chunk at the given
	 * coordinates
	 */
	@Override
	public @NotNull Collection<GeneratedStructure> getStructures(int x, int z, @NotNull Structure structure) {
		return null;
	}

	/**
	 * Gets the unique name of this world
	 *
	 * @return Name of this world
	 */
	@Override
	public @NotNull String getName() {
		return this.creator.name();
	}

	/**
	 * Gets the Unique ID of this world
	 *
	 * @return Unique ID of this world.
	 */
	@Override
	public @NotNull UUID getUID() {
		return this.uuid;
	}

	/**
	 * Gets the {@link Environment} type of this world
	 *
	 * @return This worlds Environment type
	 */
	@Override
	public @NotNull World.Environment getEnvironment() {
		return this.creator.environment();
	}

	/**
	 * Gets the Seed for this world.
	 *
	 * @return This worlds Seed
	 */
	@Override
	public long getSeed() {
		return this.creator.seed();
	}

	/**
	 * Gets the minimum height of this world.
	 * <p>
	 * If the min height is 0, there are only blocks from y=0.
	 *
	 * @return Minimum height of the world
	 */
	@Override
	public int getMinHeight() {
		return 0;
	}

	/**
	 * Gets the maximum height of this world.
	 * <p>
	 * If the max height is 100, there are only blocks from y=0 to y=99.
	 *
	 * @return Maximum height of the world
	 */
	@Override
	public int getMaxHeight() {
		return 0;
	}

	/**
	 * Sets a metadata value in the implementing object's metadata store.
	 *
	 * @param metadataKey      A unique key to identify this metadata.
	 * @param newMetadataValue The metadata value to apply.
	 * @throws IllegalArgumentException If value is null, or the owning plugin
	 *                                  is null
	 */
	@Override
	public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {

	}

	/**
	 * Returns a list of previously set metadata values from the implementing
	 * object's metadata store.
	 *
	 * @param metadataKey the unique metadata key being sought.
	 * @return A list of values, one for each plugin that has set the
	 * requested value.
	 */
	@Override
	public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
		return null;
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
		return false;
	}

	/**
	 * Removes the given metadata value from the implementing object's
	 * metadata store.
	 *
	 * @param metadataKey  the unique metadata key identifying the metadata to
	 *                     remove.
	 * @param owningPlugin This plugin's metadata value will be removed. All
	 *                     other values will be left untouched.
	 * @throws IllegalArgumentException If plugin is null
	 */
	@Override
	public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {

	}

	/**
	 * Returns a custom tag container capable of storing tags on the object.
	 * <p>
	 * Note that the tags stored on this container are all stored under their
	 * own custom namespace therefore modifying default tags using this
	 * {@link PersistentDataHolder} is impossible.
	 *
	 * @return the persistent metadata container
	 */
	@Override
	public @NotNull PersistentDataContainer getPersistentDataContainer() {
		return null;
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
	public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {

	}

	/**
	 * Gets a set containing all the Plugin Channels that this client is
	 * listening on.
	 *
	 * @return Set containing all the channels that this client may accept.
	 */
	@Override
	public @NotNull Set<String> getListeningPluginChannels() {
		return null;
	}
}
