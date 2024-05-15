package app.prismarine.server.lists;

import app.prismarine.server.player.PrismarineOfflinePlayer;
import app.prismarine.server.player.PrismarinePlayerProfile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.UUID;

public abstract class PlayerList {

	@Getter
	private final HashSet<OfflinePlayer> players = new HashSet<>();

	@Getter
	private final JsonArray entries;

	@Getter
	private final File file;

	@SneakyThrows
	public PlayerList(File file) {
		this.file = file;
		boolean created = this.file.createNewFile();

		if (created || Files.readAllBytes(file.toPath()).length == 0) {
			Files.write(file.toPath(), "[]".getBytes());
		}

		this.entries = JsonParser.parseReader(new FileReader(file)).getAsJsonArray();

		this.entries.forEach(entry -> {
			JsonObject object = (JsonObject) entry;

			String uuid = object.get("uuid").getAsString();
			String name = object.get("name").getAsString();

			this.players.add(new PrismarineOfflinePlayer(
				new PrismarinePlayerProfile(name, UUID.fromString(uuid))));
		});

		this.save();
	}

	/**
	 * Saves the list to disk
	 */
	public abstract void save();

	public final boolean contains(OfflinePlayer player) {
		for (OfflinePlayer candidate : this.players) {
			if (candidate.equals(player)) {
				return true;
			}
		}

		return false;
	}

	public final void add(OfflinePlayer player) {
		this.players.add(player);
	}

	public final void remove(OfflinePlayer player) {
		this.players.remove(player);
	}
}
