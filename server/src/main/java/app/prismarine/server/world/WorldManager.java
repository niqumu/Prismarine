package app.prismarine.server.world;

import app.prismarine.server.PrismarineServer;
import lombok.Getter;
import org.bukkit.World;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

	private void loadWorldFromDisk(File directory) {
		PrismarineServer.LOGGER.info("Loading world \"{}\"", directory.getName());
	}

	public void addWorld(World world) {
		this.worlds.add(world);
	}
}
