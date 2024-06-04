package app.prismarine.server.world;

import app.prismarine.server.PrismarineServer;
import dev.dewy.nbt.tags.collection.CompoundTag;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class WorldManager {

	@Getter
	private final List<World> worlds = new ArrayList<>();

	/**
	 * Searches the current directory to look for world saves
	 */
	public void discoverWorlds() {
		File cd = new File(".");

		// Iterate over files in the current directory
		for (File file : Objects.requireNonNull(cd.listFiles())) {
			if (!file.isDirectory()) {
				continue; // Skip files
			}

			// If level.dat exists, attempt to load the directory as a world
			if (new File(file, "level.dat").exists()) {
				this.loadWorldFromDisk(file);
			}
		}
	}

	/**
	 * Loads in a world from disk
	 * @param directory The directory containing the world
	 */
	@SneakyThrows @SuppressWarnings("deprecation")
	private void loadWorldFromDisk(File directory) {
		PrismarineServer.LOGGER.info("Loading world \"{}\"", directory.getName());

		CompoundTag data = PrismarineServer.NBT.fromFile(new File(directory, "level.dat")).getCompound("Data");

		PrismarineWorld world = new PrismarineWorld((PrismarineServer) Bukkit.getServer(),
			new WorldCreator(data.getString("LevelName").getValue()));

		world.setDifficulty(Objects.requireNonNull(Difficulty.getByValue(data.getByte("Difficulty").getValue())));
		world.setHardcore(data.getByte("hardcore").getValue() == 1);
		world.setFullTime(data.getLong("Time").getValue());

		world.setSpawnLocation(
			data.getInt("SpawnX").getValue(), data.getInt("SpawnY").getValue(),
			data.getInt("SpawnZ").getValue(), data.getFloat("SpawnAngle").getValue()
		);

		this.addWorld(world);
	}

	public void addWorld(World world) {
		this.worlds.add(world);
	}

	/**
	 * Gets the world with the given name.
	 *
	 * @param name the name of the world to retrieve
	 * @return a world with the given name, or null if none exists
	 */
	@Nullable
	public World getWorld(@NotNull String name) {
		for (World world : this.worlds) {
			if (world.getName().equals(name)) {
				return world;
			}
		}

		return null;
	}

	/**
	 * Gets the world from the given Unique ID.
	 *
	 * @param uid a unique-id of the world to retrieve
	 * @return a world with the given Unique ID, or null if none exists
	 */
	@Nullable
	public World getWorld(@NotNull UUID uid) {
		for (World world : this.worlds) {
			if (world.getUID().equals(uid)) {
				return world;
			}
		}

		return null;
	}

	/**
	 * @return The server's default world
	 */
	public World getDefaultWorld() {
		return this.worlds.get(0);
	}
}
