package app.prismarine.server.world;

import lombok.Getter;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class WorldManager {

	@Getter
	private final List<World> worlds = new ArrayList<>();

	public void discoverWorlds() {

	}
}
