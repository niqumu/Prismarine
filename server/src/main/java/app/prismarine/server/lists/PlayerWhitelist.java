package app.prismarine.server.lists;

import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;

public class PlayerWhitelist extends PlayerList {

	@SneakyThrows
	public PlayerWhitelist() {
		super(new File("whitelist.json"));
	}

	/**
	 * Saves the list to disk
	 */
	@Override @SneakyThrows
	public void save() {
		Files.write(this.getFile().toPath(), this.getEntries().toString().getBytes());
	}
}
