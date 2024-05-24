package app.prismarine.server.world;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PrismarineChunk implements Chunk {

	/**
	 * Chunk coordinates, not block coordinates
	 */
	private final int x, z;

	/**
	 * The world this chunk belongs to
	 */
	private final World world;

	public PrismarineChunk(int x, int z, World world) {
		this.x = x;
		this.z = z;
		this.world = world;
	}

	/**
	 * Gets the X-coordinate of this chunk
	 *
	 * @return X-coordinate
	 */
	@Override
	public int getX() {
		return this.x;
	}

	/**
	 * Gets the Z-coordinate of this chunk
	 *
	 * @return Z-coordinate
	 */
	@Override
	public int getZ() {
		return this.z;
	}

	/**
	 * Gets the world containing this chunk
	 *
	 * @return Parent World
	 */
	@Override
	public @NotNull World getWorld() {
		return this.world;
	}

	/**
	 * Gets a block from this chunk
	 *
	 * @param x 0-15
	 * @param y world minHeight (inclusive) - world maxHeight (exclusive)
	 * @param z 0-15
	 * @return the Block
	 */
	@Override
	public @NotNull Block getBlock(int x, int y, int z) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Capture thread-safe read-only snapshot of chunk data
	 *
	 * @return ChunkSnapshot
	 */
	@Override
	public @NotNull ChunkSnapshot getChunkSnapshot() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Capture thread-safe read-only snapshot of chunk data
	 *
	 * @param includeMaxblocky     - if true, snapshot includes per-coordinate
	 *                             maximum Y values
	 * @param includeBiome         - if true, snapshot includes per-coordinate biome
	 *                             type
	 * @param includeBiomeTempRain - if true, snapshot includes per-coordinate
	 *                             raw biome temperature and rainfall
	 * @return ChunkSnapshot
	 */
	@Override
	public @NotNull ChunkSnapshot getChunkSnapshot(boolean includeMaxblocky,
	                                               boolean includeBiome, boolean includeBiomeTempRain) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks if entities in this chunk are loaded.
	 *
	 * @return True if entities are loaded.
	 */
	@Override
	public boolean isEntitiesLoaded() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get a list of all entities in the chunk.
	 * This will force load any entities, which are not loaded.
	 *
	 * @return The entities.
	 */
	@Override
	public @NotNull Entity[] getEntities() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get a list of all tile entities in the chunk.
	 *
	 * @return The tile entities.
	 */
	@Override
	public @NotNull BlockState[] getTileEntities() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks if the chunk is fully generated.
	 *
	 * @return True if it is fully generated.
	 */
	@Override
	public boolean isGenerated() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks if the chunk is loaded.
	 *
	 * @return True if it is loaded.
	 */
	@Override
	public boolean isLoaded() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Loads the chunk.
	 *
	 * @param generate Whether or not to generate a chunk if it doesn't
	 *                 already exist
	 * @return true if the chunk has loaded successfully, otherwise false
	 */
	@Override
	public boolean load(boolean generate) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Loads the chunk.
	 *
	 * @return true if the chunk has loaded successfully, otherwise false
	 */
	@Override
	public boolean load() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Unloads and optionally saves the Chunk
	 *
	 * @param save Controls whether the chunk is saved
	 * @return true if the chunk has unloaded successfully, otherwise false
	 */
	@Override
	public boolean unload(boolean save) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Unloads and optionally saves the Chunk
	 *
	 * @return true if the chunk has unloaded successfully, otherwise false
	 */
	@Override
	public boolean unload() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks if this chunk can spawn slimes without being a swamp biome.
	 *
	 * @return true if slimes are able to spawn in this chunk
	 */
	@Override
	public boolean isSlimeChunk() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the chunk at the specified chunk coordinates is force
	 * loaded.
	 * <p>
	 * A force loaded chunk will not be unloaded due to lack of player activity.
	 *
	 * @return force load status
	 * @see World#isChunkForceLoaded(int, int)
	 */
	@Override
	public boolean isForceLoaded() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets whether the chunk at the specified chunk coordinates is force
	 * loaded.
	 * <p>
	 * A force loaded chunk will not be unloaded due to lack of player activity.
	 *
	 * @param forced force load status
	 * @see World#setChunkForceLoaded(int, int, boolean)
	 */
	@Override
	public void setForceLoaded(boolean forced) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds a plugin ticket for this chunk, loading this chunk if it is not
	 * already loaded.
	 * <p>
	 * A plugin ticket will prevent a chunk from unloading until it is
	 * explicitly removed. A plugin instance may only have one ticket per chunk,
	 * but each chunk can have multiple plugin tickets.
	 * </p>
	 *
	 * @param plugin Plugin which owns the ticket
	 * @return {@code true} if a plugin ticket was added, {@code false} if the
	 * ticket already exists for the plugin
	 * @throws IllegalStateException If the specified plugin is not enabled
	 * @see World#addPluginChunkTicket(int, int, Plugin)
	 */
	@Override
	public boolean addPluginChunkTicket(@NotNull Plugin plugin) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Removes the specified plugin's ticket for this chunk
	 * <p>
	 * A plugin ticket will prevent a chunk from unloading until it is
	 * explicitly removed. A plugin instance may only have one ticket per chunk,
	 * but each chunk can have multiple plugin tickets.
	 * </p>
	 *
	 * @param plugin Plugin which owns the ticket
	 * @return {@code true} if the plugin ticket was removed, {@code false} if
	 * there is no plugin ticket for the chunk
	 * @see World#removePluginChunkTicket(int, int, Plugin)
	 */
	@Override
	public boolean removePluginChunkTicket(@NotNull Plugin plugin) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Retrieves a collection specifying which plugins have tickets for this
	 * chunk. This collection is not updated when plugin tickets are added or
	 * removed to this chunk.
	 * <p>
	 * A plugin ticket will prevent a chunk from unloading until it is
	 * explicitly removed. A plugin instance may only have one ticket per chunk,
	 * but each chunk can have multiple plugin tickets.
	 * </p>
	 *
	 * @return unmodifiable collection containing which plugins have tickets for
	 * this chunk
	 * @see World#getPluginChunkTickets(int, int)
	 */
	@Override
	public @NotNull Collection<Plugin> getPluginChunkTickets() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the amount of time in ticks that this chunk has been inhabited.
	 * <p>
	 * Note that the time is incremented once per tick per player within mob
	 * spawning distance of this chunk.
	 *
	 * @return inhabited time
	 */
	@Override
	public long getInhabitedTime() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the amount of time in ticks that this chunk has been inhabited.
	 *
	 * @param ticks new inhabited time
	 */
	@Override
	public void setInhabitedTime(long ticks) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Tests if this chunk contains the specified block.
	 *
	 * @param block block to test
	 * @return if the block is contained within
	 */
	@Override
	public boolean contains(@NotNull BlockData block) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Tests if this chunk contains the specified biome.
	 *
	 * @param biome biome to test
	 * @return if the biome is contained within
	 */
	@Override
	public boolean contains(@NotNull Biome biome) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the load level of this chunk, which determines what game logic is
	 * processed.
	 *
	 * @return the load level
	 */
	@Override
	public @NotNull LoadLevel getLoadLevel() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets all generated structures that intersect this chunk. <br>
	 * If no structures are present an empty collection will be returned.
	 *
	 * @return a collection of placed structures in this chunk
	 */
	@Override
	public @NotNull Collection<GeneratedStructure> getStructures() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets all generated structures of a given {@link Structure} that intersect
	 * this chunk. <br>
	 * If no structures are present an empty collection will be returned.
	 *
	 * @param structure the structure to find
	 * @return a collection of placed structures in this chunk
	 */
	@Override
	public @NotNull Collection<GeneratedStructure> getStructures(@NotNull Structure structure) {
		throw new UnsupportedOperationException("Not yet implemented");
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
		throw new UnsupportedOperationException("Not yet implemented");
	}
}