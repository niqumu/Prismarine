package app.prismarine.server;

import app.prismarine.server.entity.EntityManager;
import app.prismarine.server.lists.PlayerWhitelist;
import app.prismarine.server.net.NettyServer;
import app.prismarine.server.net.packet.PacketManager;
import app.prismarine.server.player.PrismarinePlayerProfile;
import app.prismarine.server.scheduler.TickThread;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.*;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.packs.DataPackManager;
import org.bukkit.packs.ResourcePack;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.ChannelNotRegisteredException;
import org.bukkit.plugin.messaging.MessageTooLargeException;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.CachedServerIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.util.*;
import java.util.function.Consumer;

/**
 * Main class of the Prismarine server
 */
public final class PrismarineServer implements Server {

	/**
	 * The name and version of the server implementation
	 */
	public static final String SERVER_IMPLEMENTATION = "Prismarine";
	public static final String SERVER_VERSION = "1.0-SNAPSHOT";

	/**
	 * The Spigot API version
	 */
	public static final String API_VERSION = "1.20.6-R0.1-SNAPSHOT";

	/**
	 * The game and protocol version the server is targeting
	 */
	public static final String GAME_VERSION = "1.20.6";
	public static final int PROTOCOL_VERSION = 766;

	/**
	 * The global logger
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(PrismarineServer.class);

	/**
	 * The global JUL logger, provided for legacy reasons (Bukkit requires this)
	 */
	private static final java.util.logging.Logger BUKKIT_LOGGER =
		java.util.logging.Logger.getLogger("Bukkit API");
	static { // Set up the SLF4J bridge from JUL
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

	/**
	 * Whether the server is currently alive or not
	 */
	private boolean running = false;

	/**
	 * The server's {@link NettyServer} for handling networking and connections
	 */
	private NettyServer nettyServer;

	/**
	 * The server's {@link ServerConfig} instance
	 */
	private ServerConfig config;

	/**
	 * The server's {@link EntityManager} instance, responsible for managing, ticking, and registering entities
	 */
	@Getter
	private final EntityManager entityManager = new EntityManager();

	/**
	 * The server's {@link TickThread} instance
	 */
	@Getter
	private final TickThread tickThread = new TickThread();

	/**
	 * The server whitelist
	 */
	@Getter
	private PlayerWhitelist whitelist = new PlayerWhitelist();


	/**
	 * Starts up the server
	 */
	@SneakyThrows
	public void startup() {

		// Ensure that the server hasn't already been started
		if (this.running) {
			throw new IllegalStateException("The server is already running!");
		}

		// Set the Bukkit singleton
		Bukkit.setServer(this);

		final long startTime = System.currentTimeMillis();
		LOGGER.info("Starting Prismarine!");

		// Read the configuration
		this.config = new ServerConfig();

		// Start the netty server
		LOGGER.info("Starting server at {}:{}", this.config.getIp(), this.config.getPort());
		this.nettyServer = new NettyServer(this);
		this.nettyServer.startup();

		PacketManager.registerPackets();

		// Ignite the tick thread
		this.tickThread.start();

		this.running = true;
		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
		LOGGER.info("Done! Started Prismarine in {} ms", System.currentTimeMillis() - startTime);
	}

	/**
	 * Shutdowns the server, stopping everything.
	 */
	public void shutdown() {
		Thread.currentThread().setName("Shutdown thread");

		LOGGER.info("Stopping!");

		// Shutdown the networking server, disconnecting all players
		this.nettyServer.shutdown();

		// Stop ticking
		this.tickThread.shutdown();

		// Save the server state
		this.config.save();
	}

	public static void main(String[] args) {
		new PrismarineServer().startup();
	}

	/**
	 * Gets the name of this server implementation.
	 *
	 * @return name of this server implementation
	 */
	@Override @NonNull
	public String getName() {
		return SERVER_IMPLEMENTATION;
	}

	/**
	 * Gets the version string of this server implementation.
	 *
	 * @return version of this server implementation
	 */
	@Override @NonNull
	public String getVersion() {
		return SERVER_VERSION;
	}

	/**
	 * Gets the Bukkit version that this server is running.
	 *
	 * @return version of Bukkit
	 */
	@Override @NonNull
	public String getBukkitVersion() {
		return API_VERSION;
	}

	/**
	 * Gets a view of all currently logged in players.
	 *
	 * @return a view of currently online players.
	 */
	@Override @NonNull
	public Collection<? extends Player> getOnlinePlayers() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the maximum amount of players which can log into this server.
	 *
	 * @return the amount of players this server allows
	 */
	@Override
	public int getMaxPlayers() {
		return this.config.getMaxPlayers();
	}

	/**
	 * Set the maximum amount of players allowed to be logged in at once.
	 *
	 * @param maxPlayers The maximum amount of concurrent players
	 */
	@Override
	public void setMaxPlayers(int maxPlayers) {
		this.config.setMaxPlayers(maxPlayers);
	}

	/**
	 * Get the game port that the server runs on.
	 *
	 * @return the port number of this server
	 */
	@Override
	public int getPort() {
		return this.config.getPort();
	}

	/**
	 * Get the view distance from this server.
	 *
	 * @return the view distance from this server.
	 */
	@Override
	public int getViewDistance() {
		return this.config.getViewDistance();
	}

	/**
	 * Get the simulation distance from this server.
	 *
	 * @return the simulation distance from this server.
	 */
	@Override
	public int getSimulationDistance() {
		return this.config.getSimulationDistance();
	}

	/**
	 * Get the IP that this server is bound to, or empty string if not
	 * specified.
	 *
	 * @return the IP string that this server is bound to, otherwise empty
	 *     string
	 */
	@Override @NonNull
	public String getIp() {
		return this.config.getIp();
	}

	/**
	 * Get world type (level-type setting) for default world.
	 *
	 * @return the value of level-type (e.g. DEFAULT, FLAT, DEFAULT_1_1)
	 */
	@Override @NonNull
	public String getWorldType() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get generate-structures setting.
	 *
	 * @return true if structure generation is enabled, false otherwise
	 */
	@Override
	public boolean getGenerateStructures() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get generate-structures setting.
	 *
	 * @return true if structure generation is enabled, false otherwise
	 */
	@Override
	public int getMaxWorldSize() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether this server allows the End or not.
	 *
	 * @return whether this server allows the End or not
	 */
	@Override
	public boolean getAllowEnd() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether this server allows the Nether or not.
	 *
	 * @return whether this server allows the Nether or not
	 */
	@Override
	public boolean getAllowNether() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the server is logging the IP addresses of players.
	 *
	 * @return whether the server is logging the IP addresses of players
	 */
	@Override
	public boolean isLoggingIPs() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a list of packs to be enabled.
	 *
	 * @return a list of packs names
	 */
	@Override @NonNull
	public List<String> getInitialEnabledPacks() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a list of packs that will not be enabled automatically.
	 *
	 * @return a list of packs names
	 */
	@Override @NonNull
	public List<String> getInitialDisabledPacks() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the DataPack Manager.
	 *
	 * @return the manager
	 */
	@Override @NonNull
	public DataPackManager getDataPackManager() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the ServerTick Manager.
	 *
	 * @return the manager
	 */
	@Override @NonNull
	public ServerTickManager getServerTickManager() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the resource pack configured to be sent to clients by the server.
	 *
	 * @return the resource pack
	 */
	@Override
	public ResourcePack getServerResourcePack() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the server resource pack uri, or empty string if not specified.
	 *
	 * @return the server resource pack uri, otherwise empty string
	 */
	@Override @NonNull
	public String getResourcePack() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the SHA-1 digest of the server resource pack, or empty string if
	 * not specified.
	 *
	 * @return the SHA-1 digest of the server resource pack, otherwise empty
	 *     string
	 */
	@Override @NonNull
	public String getResourcePackHash() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the custom prompt message to be shown when the server resource
	 * pack is required, or empty string if not specified.
	 *
	 * @return the custom prompt message to be shown when the server resource,
	 *     otherwise empty string
	 */
	@Override @NonNull
	public String getResourcePackPrompt() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the server resource pack is enforced.
	 *
	 * @return whether the server resource pack is enforced
	 */
	@Override
	public boolean isResourcePackRequired() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether this server has a whitelist or not.
	 *
	 * @return whether this server has a whitelist or not
	 */
	@Override
	public boolean hasWhitelist() {
		return this.config.isWhitelist();
	}

	/**
	 * Sets if the server is whitelisted.
	 *
	 * @param value true for whitelist on, false for off
	 */
	@Override
	public void setWhitelist(boolean value) {
		this.config.setWhitelist(value);
	}

	/**
	 * Gets whether the server whitelist is enforced.
	 *
	 * If the whitelist is enforced, non-whitelisted players will be
	 * disconnected when the server whitelist is reloaded.
	 *
	 * @return whether the server whitelist is enforced
	 */
	@Override
	public boolean isWhitelistEnforced() {
		return false; // This is a strange redundant system that I am choosing to not implement
	}

	/**
	 * Sets if the server whitelist is enforced.
	 *
	 * If the whitelist is enforced, non-whitelisted players will be
	 * disconnected when the server whitelist is reloaded.
	 *
	 * @param value true for enforced, false for not
	 */
	@Override
	public void setWhitelistEnforced(boolean value) {

	}

	/**
	 * Gets a list of whitelisted players.
	 *
	 * @return a set containing all whitelisted players
	 */
	@Override @NonNull
	public Set<OfflinePlayer> getWhitelistedPlayers() {
		return this.whitelist.getPlayers();
	}

	/**
	 * Reloads the whitelist from disk.
	 */
	@Override
	public void reloadWhitelist() {
		this.whitelist = new PlayerWhitelist();
	}

	/**
	 * Broadcast a message to all players.
	 * <p>
	 * This is the same as calling {@link #broadcast(java.lang.String,
	 * java.lang.String)} to {@link #BROADCAST_CHANNEL_USERS}
	 *
	 * @param message the message
	 * @return the number of players
	 */
	@Override
	public int broadcastMessage(@NonNull String message) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the name of the update folder. The update folder is used to safely
	 * update plugins at the right moment on a plugin load.
	 * <p>
	 * The update folder name is relative to the plugins folder.
	 *
	 * @return the name of the update folder
	 */
	@Override @NonNull
	public String getUpdateFolder() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the update folder. The update folder is used to safely update
	 * plugins at the right moment on a plugin load.
	 *
	 * @return the update folder
	 */
	@Override @NonNull
	public File getUpdateFolderFile() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the value of the connection throttle setting.
	 *
	 * @return the value of the connection throttle setting
	 */
	@Override
	public long getConnectionThrottle() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets default ticks per animal spawns value.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn monsters
	 *     every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn monsters
	 *     every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b> If set to 0, animal spawning will be disabled. We
	 * recommend using spawn-animals to control this instead.
	 * <p>
	 * Minecraft default: 400.
	 *
	 * @return the default ticks per animal spawns value
	 * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
	 */
	@Override @Deprecated
	public int getTicksPerAnimalSpawns() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the default ticks per monster spawns value.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn monsters
	 *     every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn monsters
	 *     every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b> If set to 0, monsters spawning will be disabled. We
	 * recommend using spawn-monsters to control this instead.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @return the default ticks per monsters spawn value
	 * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
	 */
	@Override @Deprecated
	public int getTicksPerMonsterSpawns() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the default ticks per water mob spawns value.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn water mobs
	 *     every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn water mobs
	 *     every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b> If set to 0, water mobs spawning will be disabled.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @return the default ticks per water mobs spawn value
	 * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
	 */
	@Override @Deprecated
	public int getTicksPerWaterSpawns() {
		throw new UnsupportedOperationException("Not yet implemented");
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
	@Override @Deprecated
	public int getTicksPerWaterAmbientSpawns() {
		throw new UnsupportedOperationException("Not yet implemented");
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
	@Override @Deprecated
	public int getTicksPerWaterUndergroundCreatureSpawns() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the default ticks per ambient mob spawns value.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn ambient mobs
	 *     every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn ambient mobs
	 *     every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b> If set to 0, ambient mobs spawning will be disabled.
	 * <p>
	 * Minecraft default: 1.
	 *
	 * @return the default ticks per ambient mobs spawn value
	 * @deprecated Deprecated in favor of {@link #getTicksPerSpawns(SpawnCategory)}
	 */
	@Override @Deprecated
	public int getTicksPerAmbientSpawns() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the default ticks per {@link SpawnCategory} spawns value.
	 * <p>
	 * <b>Example Usage:</b>
	 * <ul>
	 * <li>A value of 1 will mean the server will attempt to spawn {@link SpawnCategory} mobs
	 *     every tick.
	 * <li>A value of 400 will mean the server will attempt to spawn {@link SpawnCategory} mobs
	 *     every 400th tick.
	 * <li>A value below 0 will be reset back to Minecraft's default.
	 * </ul>
	 * <p>
	 * <b>Note:</b> If set to 0, {@link SpawnCategory} mobs spawning will be disabled.
	 * <p>
	 * Minecraft default: 1.
	 * <br>
	 * <b>Note: </b> the {@link SpawnCategory#MISC} are not consider.
	 *
	 * @param spawnCategory the category of spawn
	 * @return the default ticks per {@link SpawnCategory} mobs spawn value
	 */
	@Override
	public int getTicksPerSpawns(@NonNull SpawnCategory spawnCategory) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a player object by the given username.
	 * <p>
	 * This method may not return objects for offline players.
	 *
	 * @param name the name to look up
	 * @return a player if one was found, null otherwise
	 */
	@Override @Nullable
	public Player getPlayer(@NonNull String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player with the exact given name, case insensitive.
	 *
	 * @param name Exact name of the player to retrieve
	 * @return a player object if one was found, null otherwise
	 */
	@Override @Nullable
	public Player getPlayerExact(@NonNull String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Attempts to match any players with the given name, and returns a list
	 * of all possibly matches.
	 * <p>
	 * This list is not sorted in any particular order. If an exact match is
	 * found, the returned list will only contain a single result.
	 *
	 * @param name the (partial) name to match
	 * @return list of all possible players
	 */
	@Override @NonNull
	public List<Player> matchPlayer(@NonNull String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player with the given UUID.
	 *
	 * @param id UUID of the player to retrieve
	 * @return a player object if one was found, null otherwise
	 */
	@Override @Nullable
	public Player getPlayer(@NonNull UUID id) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the plugin manager for interfacing with plugins.
	 *
	 * @return a plugin manager for this Server instance
	 */
	@Override @NonNull
	public PluginManager getPluginManager() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the scheduler for managing scheduled events.
	 *
	 * @return a scheduling service for this server
	 */
	@Override @NonNull
	public BukkitScheduler getScheduler() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a services manager.
	 *
	 * @return s services manager
	 */
	@Override @NonNull
	public ServicesManager getServicesManager() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a list of all worlds on this server.
	 *
	 * @return a list of worlds
	 */
	@Override @NonNull
	public List<World> getWorlds() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates or loads a world with the given name using the specified
	 * options.
	 * <p>
	 * If the world is already loaded, it will just return the equivalent of
	 * getWorld(creator.name()).
	 *
	 * @param creator the options to use when creating the world
	 * @return newly created or loaded world
	 */
	@Override @Nullable
	public World createWorld(@NonNull WorldCreator creator) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Unloads a world with the given name.
	 *
	 * @param name Name of the world to unload
	 * @param save whether to save the chunks before unloading
	 * @return true if successful, false otherwise
	 */
	@Override
	public boolean unloadWorld(@NonNull String name, boolean save) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Unloads the given world.
	 *
	 * @param world the world to unload
	 * @param save whether to save the chunks before unloading
	 * @return true if successful, false otherwise
	 */
	@Override
	public boolean unloadWorld(@NonNull World world, boolean save) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the world with the given name.
	 *
	 * @param name the name of the world to retrieve
	 * @return a world with the given name, or null if none exists
	 */
	@Override @Nullable
	public World getWorld(@NonNull String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the world from the given Unique ID.
	 *
	 * @param uid a unique-id of the world to retrieve
	 * @return a world with the given Unique ID, or null if none exists
	 */
	@Override @Nullable
	public World getWorld(@NonNull UUID uid) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Create a new virtual {@link WorldBorder}.
	 * <p>
	 * Note that world borders created by the server will not respect any world
	 * scaling effects (i.e. coordinates are not divided by 8 in the nether).
	 *
	 * @return the created world border instance
	 *
	 * @see Player#setWorldBorder(WorldBorder)
	 */
	@Override @NonNull
	public WorldBorder createWorldBorder() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the map from the given item ID.
	 *
	 * @param id the id of the map to get
	 * @return a map view if it exists, or null otherwise
	 * @deprecated Magic value
	 */
	@Override @Deprecated @Nullable
	public MapView getMap(int id) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Create a new map with an automatically assigned ID.
	 *
	 * @param world the world the map will belong to
	 * @return a newly created map view
	 */
	@Override @NonNull
	public MapView createMap(@NonNull World world) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Create a new explorer map targeting the closest nearby structure of a
	 * given {@link StructureType}.
	 * <br>
	 * This method uses implementation default values for radius and
	 * findUnexplored (usually 100, true).
	 *
	 * @param world the world the map will belong to
	 * @param location the origin location to find the nearest structure
	 * @param structureType the type of structure to find
	 * @return a newly created item stack
	 *
	 * @see World#locateNearestStructure(org.bukkit.Location,
	 *      org.bukkit.StructureType, int, boolean)
	 */
	@Override @NonNull
	public ItemStack createExplorerMap(@NonNull World world, @NonNull Location location,
	                                   @NonNull StructureType structureType) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Create a new explorer map targeting the closest nearby structure of a
	 * given {@link StructureType}.
	 * <br>
	 * This method uses implementation default values for radius and
	 * findUnexplored (usually 100, true).
	 *
	 * @param world the world the map will belong to
	 * @param location the origin location to find the nearest structure
	 * @param structureType the type of structure to find
	 * @param radius radius to search, see World#locateNearestStructure for more
	 *               information
	 * @param findUnexplored whether to find unexplored structures
	 * @return the newly created item stack
	 *
	 * @see World#locateNearestStructure(org.bukkit.Location,
	 *      org.bukkit.StructureType, int, boolean)
	 */
	@Override @NonNull
	public ItemStack createExplorerMap(@NonNull World world, @NonNull Location location,
	                                   @NonNull StructureType structureType, int radius, boolean findUnexplored) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Reloads the server, refreshing settings and plugin information.
	 */
	@Override
	public void reload() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Reload only the Minecraft data for the server. This includes custom
	 * advancements and loot tables.
	 */
	@Override
	public void reloadData() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the primary logger associated with this server instance.
	 *
	 * @return Logger associated with this server
	 */
	@Override @NonNull
	public java.util.logging.Logger getLogger() {
		return BUKKIT_LOGGER;
	}

	/**
	 * Gets a {@link PluginCommand} with the given name or alias.
	 *
	 * @param name the name of the command to retrieve
	 * @return a plugin command if found, null otherwise
	 */
	@Override @Nullable
	public PluginCommand getPluginCommand(@NonNull String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Writes loaded players to disk.
	 */
	@Override
	public void savePlayers() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Dispatches a command on this server, and executes it if found.
	 *
	 * @param sender the apparent sender of the command
	 * @param commandLine the command + arguments. Example: <code>test abc
	 *     123</code>
	 * @return returns false if no target is found
	 * @throws CommandException thrown when the executor for the given command
	 *     fails with an unhandled exception
	 */
	@Override
	public boolean dispatchCommand(@NonNull CommandSender sender,
	                               @NonNull String commandLine) throws CommandException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Adds a recipe to the crafting manager.
	 *
	 * @param recipe the recipe to add
	 * @return true if the recipe was added, false if it wasn't for some
	 *     reason
	 */
	@Override
	public boolean addRecipe(Recipe recipe) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get a list of all recipes for a given item. The stack size is ignored
	 * in comparisons. If the durability is -1, it will match any data value.
	 *
	 * @param result the item to match against recipe results
	 * @return a list of recipes with the given result
	 */
	@Override @NonNull
	public List<Recipe> getRecipesFor(@NonNull ItemStack result) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Recipe} for the given key.
	 *
	 * @param recipeKey the key of the recipe to return
	 * @return the recipe for the given key or null.
	 */
	@Override @Nullable
	public Recipe getRecipe(@NonNull NamespacedKey recipeKey) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the {@link Recipe} for the list of ItemStacks provided.
	 *
	 * <p>The list is formatted as a crafting matrix where the index follow
	 * the pattern below:</p>
	 *
	 * <pre>
	 * [ 0 1 2 ]
	 * [ 3 4 5 ]
	 * [ 6 7 8 ]
	 * </pre>
	 *
	 * <p>NOTE: This method will not modify the provided ItemStack array, for that, use
	 * {@link #craftItem(ItemStack[], World, Player)}.</p>
	 *
	 * @param craftingMatrix list of items to be crafted from.
	 *                       Must not contain more than 9 items.
	 * @param world The world the crafting takes place in.
	 * @return the {@link Recipe} resulting from the given crafting matrix.
	 */
	@Override @Nullable
	public Recipe getCraftingRecipe(@NonNull ItemStack[] craftingMatrix, @NonNull World world) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the crafted item using the list of {@link ItemStack} provided.
	 *
	 * <p>The list is formatted as a crafting matrix where the index follow
	 * the pattern below:</p>
	 *
	 * <pre>
	 * [ 0 1 2 ]
	 * [ 3 4 5 ]
	 * [ 6 7 8 ]
	 * </pre>
	 *
	 * <p>The {@link World} and {@link Player} arguments are required to fulfill the Bukkit Crafting
	 * events.</p>
	 *
	 * <p>Calls {@link org.bukkit.event.inventory.PrepareItemCraftEvent} to imitate the {@link Player}
	 * initiating the crafting event.</p>
	 *
	 * @param craftingMatrix list of items to be crafted from.
	 *                       Must not contain more than 9 items.
	 * @param world The world the crafting takes place in.
	 * @param player The player to imitate the crafting event on.
	 * @return the {@link ItemStack} resulting from the given crafting matrix, if no recipe is found
	 * an ItemStack of {@link Material#AIR} is returned.
	 */
	@Override @NonNull
	public ItemStack craftItem(@NonNull ItemStack[] craftingMatrix, @NonNull World world, @NonNull Player player) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the crafted item using the list of {@link ItemStack} provided.
	 *
	 * <p>The list is formatted as a crafting matrix where the index follow
	 * the pattern below:</p>
	 *
	 * <pre>
	 * [ 0 1 2 ]
	 * [ 3 4 5 ]
	 * [ 6 7 8 ]
	 * </pre>
	 *
	 * @param craftingMatrix list of items to be crafted from.
	 *                       Must not contain more than 9 items.
	 * @param world The world the crafting takes place in.
	 * @return the {@link ItemStack} resulting from the given crafting matrix, if no recipe is found
	 * an ItemStack of {@link Material#AIR} is returned.
	 */
	@Override @NonNull
	public ItemStack craftItem(@NonNull ItemStack[] craftingMatrix, @NonNull World world) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the crafted item using the list of {@link ItemStack} provided.
	 *
	 * <p>The list is formatted as a crafting matrix where the index follow
	 * the pattern below:</p>
	 *
	 * <pre>
	 * [ 0 1 2 ]
	 * [ 3 4 5 ]
	 * [ 6 7 8 ]
	 * </pre>
	 *
	 * <p>The {@link World} and {@link Player} arguments are required to fulfill the Bukkit Crafting
	 * events.</p>
	 *
	 * <p>Calls {@link org.bukkit.event.inventory.PrepareItemCraftEvent} to imitate the {@link Player}
	 * initiating the crafting event.</p>
	 *
	 * @param craftingMatrix list of items to be crafted from.
	 *                       Must not contain more than 9 items.
	 * @param world The world the crafting takes place in.
	 * @param player The player to imitate the crafting event on.
	 * @return resulting {@link ItemCraftResult} containing the resulting item, matrix and any overflow items.
	 */
	@Override @NonNull
	public ItemCraftResult craftItemResult(@NonNull ItemStack[] craftingMatrix, @NonNull World world,
	                                       @NonNull Player player) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the crafted item using the list of {@link ItemStack} provided.
	 *
	 * <p>The list is formatted as a crafting matrix where the index follow
	 * the pattern below:</p>
	 *
	 * <pre>
	 * [ 0 1 2 ]
	 * [ 3 4 5 ]
	 * [ 6 7 8 ]
	 * </pre>
	 *
	 * @param craftingMatrix list of items to be crafted from.
	 *                       Must not contain more than 9 items.
	 * @param world The world the crafting takes place in.
	 * @return resulting {@link ItemCraftResult} containing the resulting item, matrix and any overflow items.
	 */
	@Override @NonNull
	public ItemCraftResult craftItemResult(@NonNull ItemStack[] craftingMatrix, @NonNull World world) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get an iterator through the list of crafting recipes.
	 *
	 * @return an iterator
	 */
	@Override @NonNull
	public Iterator<Recipe> recipeIterator() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Clears the list of crafting recipes.
	 */
	@Override
	public void clearRecipes() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Resets the list of crafting recipes to the default.
	 */
	@Override
	public void resetRecipes() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Remove a recipe from the server.
	 *
	 * <b>Note that removing a recipe may cause permanent loss of data
	 * associated with that recipe (eg whether it has been discovered by
	 * players).</b>
	 *
	 * @param key NamespacedKey of recipe to remove.
	 * @return True if recipe was removed
	 */
	@Override
	public boolean removeRecipe(@NonNull NamespacedKey key) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a list of command aliases defined in the server properties.
	 *
	 * @return a map of aliases to command names
	 */
	@Override @NonNull
	public Map<String, String[]> getCommandAliases() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the radius, in blocks, around each worlds spawn point to protect.
	 *
	 * @return spawn radius, or 0 if none
	 */
	@Override
	public int getSpawnRadius() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the radius, in blocks, around each worlds spawn point to protect.
	 *
	 * @param value new spawn radius, or 0 if none
	 */
	@Override
	public void setSpawnRadius(int value) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the server should send a preview of the player's chat
	 * message to the client when the player types a message
	 *
	 * @return true if the server should send a preview, false otherwise
	 * @deprecated chat previews have been removed
	 */
	@Override @Deprecated
	public boolean shouldSendChatPreviews() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the server only allow players with Mojang-signed public key
	 * to join
	 *
	 * @return true if only Mojang-signed players can join, false otherwise
	 */
	@Override
	public boolean isEnforcingSecureProfiles() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether this server is allowing connections transferred from other
	 * servers.
	 *
	 * @return true if the server accepts transfers, false otherwise
	 */
	@Override
	public boolean isAcceptingTransfers() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the Server hide online players in server status.
	 *
	 * @return true if the server hide online players, false otherwise
	 */
	@Override
	public boolean getHideOnlinePlayers() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the Server is in online mode or not.
	 *
	 * @return true if the server authenticates clients, false otherwise
	 */
	@Override
	public boolean getOnlineMode() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether this server allows flying or not.
	 *
	 * @return true if the server allows flight, false otherwise
	 */
	@Override
	public boolean getAllowFlight() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets whether the server is in hardcore mode or not.
	 *
	 * @return true if the server mode is hardcore, false otherwise
	 */
	@Override
	public boolean isHardcore() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Broadcasts the specified message to every user with the given
	 * permission name.
	 *
	 * @param message message to broadcast
	 * @param permission the required permission {@link Permissible
	 *     permissibles} must have to receive the broadcast
	 * @return number of message recipients
	 */
	@Override
	public int broadcast(@NonNull String message, @NonNull String permission) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player by the given name, regardless if they are offline or
	 * online.
	 * <p>
	 * This method may involve a blocking web request to get the UUID for the
	 * given name.
	 * <p>
	 * This will return an object even if the player does not exist. To this
	 * method, all players will exist.
	 *
	 * @param name the name the player to retrieve
	 * @return an offline player
	 * @see #getOfflinePlayer(java.util.UUID)
	 * @deprecated Persistent storage of users should be by UUID as names are no longer
	 *             unique past a single session.
	 */
	@Override @Deprecated @NonNull
	public OfflinePlayer getOfflinePlayer(@NonNull String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the player by the given UUID, regardless if they are offline or
	 * online.
	 * <p>
	 * This will return an object even if the player does not exist. To this
	 * method, all players will exist.
	 *
	 * @param id the UUID of the player to retrieve
	 * @return an offline player
	 */
	@Override @NonNull
	public OfflinePlayer getOfflinePlayer(UUID id) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates a new {@link PlayerProfile}.
	 *
	 * @param uniqueId the unique id
	 * @param name the name
	 * @return the new PlayerProfile
	 * @throws IllegalArgumentException if both the unique id is
	 * <code>null</code> and the name is <code>null</code> or blank
	 */
	@Override @NonNull
	public PlayerProfile createPlayerProfile(@Nullable UUID uniqueId, @Nullable String name) {
		if (uniqueId == null && (name == null || name.isEmpty())) {
			throw new IllegalArgumentException("uniqueId and name cannot both be null/empty!");
		}

		return new PrismarinePlayerProfile(name, uniqueId);
	}

	/**
	 * Creates a new {@link PlayerProfile}.
	 *
	 * @param uniqueId the unique id
	 * @return the new PlayerProfile
	 * @throws IllegalArgumentException if the unique id is <code>null</code>
	 */
	@Override @NonNull
	public PlayerProfile createPlayerProfile(@NonNull UUID uniqueId) {
		return new PrismarinePlayerProfile(null, uniqueId);
	}

	/**
	 * Creates a new {@link PlayerProfile}.
	 *
	 * @param name the name
	 * @return the new PlayerProfile
	 * @throws IllegalArgumentException if the name is <code>null</code> or
	 * blank
	 */
	@Override @NonNull
	public PlayerProfile createPlayerProfile(@NonNull String name) {
		return new PrismarinePlayerProfile(name, null);
	}

	/**
	 * Gets a set containing all current IPs that are banned.
	 *
	 * @return a set containing banned IP addresses
	 */
	@Override @NonNull
	public Set<String> getIPBans() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Bans the specified address from the server.
	 *
	 * @param address the IP address to ban
	 *
	 * @deprecated see {@link #banIP(InetAddress)}
	 */
	@Override @Deprecated
	public void banIP(@NonNull String address) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Unbans the specified address from the server.
	 *
	 * @param address the IP address to unban
	 *
	 * @deprecated see {@link #unbanIP(InetAddress)}
	 */
	@Override @Deprecated
	public void unbanIP(@NonNull String address) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Bans the specified address from the server.
	 *
	 * @param address the IP address to ban
	 */
	@Override
	public void banIP(@NonNull InetAddress address) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Unbans the specified address from the server.
	 *
	 * @param address the IP address to unban
	 */
	@Override
	public void unbanIP(@NonNull InetAddress address) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a set containing all banned players.
	 *
	 * @return a set containing banned players
	 */
	@Override @NonNull
	public Set<OfflinePlayer> getBannedPlayers() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a ban list for the supplied type.
	 *
	 * @param type the type of list to fetch, cannot be null
	 * @param <T> The ban target
	 *
	 * @return a ban list of the specified type
	 */
	@Override @NonNull
	public <T extends BanList<?>> T getBanList(@NonNull BanList.Type type) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a set containing all player operators.
	 *
	 * @return a set containing player operators
	 */
	@Override @NonNull
	public Set<OfflinePlayer> getOperators() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the default {@link GameMode} for new players.
	 *
	 * @return the default game mode
	 */
	@Override @NonNull
	public GameMode getDefaultGameMode() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sets the default {@link GameMode} for new players.
	 *
	 * @param mode the new game mode
	 */
	@Override @NonNull
	public void setDefaultGameMode(@NonNull GameMode mode) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a {@link ConsoleCommandSender} that may be used as an input source
	 * for this server.
	 *
	 * @return a console command sender
	 */
	@Override @NonNull
	public ConsoleCommandSender getConsoleSender() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the folder that contains all of the various {@link World}s.
	 *
	 * @return folder that contains all worlds
	 */
	@Override @NonNull
	public File getWorldContainer() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets every player that has ever played on this server.
	 *
	 * @return an array containing all previous players
	 */
	@Override @NonNull
	public OfflinePlayer[] getOfflinePlayers() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the {@link Messenger} responsible for this server.
	 *
	 * @return messenger responsible for this server
	 */
	@Override @NonNull
	public Messenger getMessenger() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the {@link HelpMap} providing help topics for this server.
	 *
	 * @return a help map for this server
	 */
	@Override @NonNull
	public HelpMap getHelpMap() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates an empty inventory with the specified type. If the type
	 * is {@link InventoryType#CHEST}, the new inventory has a size of 27;
	 * otherwise the new inventory has the normal size for its type.
	 * <br>
	 * {@link InventoryType#WORKBENCH} will not process crafting recipes if
	 * created with this method. Use
	 * {@link Player#openWorkbench(Location, boolean)} instead.
	 * <br>
	 * {@link InventoryType#ENCHANTING} will not process {@link ItemStack}s
	 * for possible enchanting results. Use
	 * {@link Player#openEnchanting(Location, boolean)} instead.
	 *
	 * @param owner the holder of the inventory, or null to indicate no holder
	 * @param type the type of inventory to create
	 * @return a new inventory
	 * @throws IllegalArgumentException if the {@link InventoryType} cannot be
	 * viewed.
	 *
	 * @see InventoryType#isCreatable()
	 */
	@Override @NonNull
	public Inventory createInventory(@Nullable InventoryHolder owner, @NonNull InventoryType type) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates an empty inventory with the specified type and title. If the type
	 * is {@link InventoryType#CHEST}, the new inventory has a size of 27;
	 * otherwise the new inventory has the normal size for its type.<br>
	 * It should be noted that some inventory types do not support titles and
	 * may not render with said titles on the Minecraft client.
	 * <br>
	 * {@link InventoryType#WORKBENCH} will not process crafting recipes if
	 * created with this method. Use
	 * {@link Player#openWorkbench(Location, boolean)} instead.
	 * <br>
	 * {@link InventoryType#ENCHANTING} will not process {@link ItemStack}s
	 * for possible enchanting results. Use
	 * {@link Player#openEnchanting(Location, boolean)} instead.
	 *
	 * @param owner The holder of the inventory; can be null if there's no holder.
	 * @param type The type of inventory to create.
	 * @param title The title of the inventory, to be displayed when it is viewed.
	 * @return The new inventory.
	 * @throws IllegalArgumentException if the {@link InventoryType} cannot be
	 * viewed.
	 *
	 * @see InventoryType#isCreatable()
	 */
	@Override @NonNull
	public Inventory createInventory(@Nullable InventoryHolder owner,
	                                 @NonNull InventoryType type, @NonNull String title) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates an empty inventory of type {@link InventoryType#CHEST} with the
	 * specified size.
	 *
	 * @param owner the holder of the inventory, or null to indicate no holder
	 * @param size a multiple of 9 as the size of inventory to create
	 * @return a new inventory
	 * @throws IllegalArgumentException if the size is not a multiple of 9
	 */
	@Override @NonNull
	public Inventory createInventory(@Nullable InventoryHolder owner, int size) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates an empty inventory of type {@link InventoryType#CHEST} with the
	 * specified size and title.
	 *
	 * @param owner the holder of the inventory, or null to indicate no holder
	 * @param size a multiple of 9 as the size of inventory to create
	 * @param title the title of the inventory, displayed when inventory is
	 *     viewed
	 * @return a new inventory
	 * @throws IllegalArgumentException if the size is not a multiple of 9
	 */
	@Override @NonNull
	public Inventory createInventory(@Nullable InventoryHolder owner, int size,
	                                 @NonNull String title) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates an empty merchant.
	 *
	 * @param title the title of the corresponding merchant inventory, displayed
	 * when the merchant inventory is viewed
	 * @return a new merchant
	 */
	@Override @NonNull
	public Merchant createMerchant(@Nullable String title) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the amount of consecutive neighbor updates before skipping
	 * additional ones.
	 *
	 * @return the amount of consecutive neighbor updates, if the value is
	 * negative then the limit it's not used
	 */
	@Override
	public int getMaxChainedNeighborUpdates() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets user-specified limit for number of monsters that can spawn in a
	 * chunk.
	 *
	 * @return the monster spawn limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override @Deprecated
	public int getMonsterSpawnLimit() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets user-specified limit for number of animals that can spawn in a
	 * chunk.
	 *
	 * @return the animal spawn limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override @Deprecated
	public int getAnimalSpawnLimit() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets user-specified limit for number of water animals that can spawn in
	 * a chunk.
	 *
	 * @return the water animal spawn limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override @Deprecated
	public int getWaterAnimalSpawnLimit() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets user-specified limit for number of water ambient mobs that can spawn
	 * in a chunk.
	 *
	 * @return the water ambient spawn limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override @Deprecated
	public int getWaterAmbientSpawnLimit() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get user-specified limit for number of water creature underground that can spawn
	 * in a chunk.
	 * @return the water underground creature limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override @Deprecated
	public int getWaterUndergroundCreatureSpawnLimit() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets user-specified limit for number of ambient mobs that can spawn in
	 * a chunk.
	 *
	 * @return the ambient spawn limit
	 * @deprecated Deprecated in favor of {@link #getSpawnLimit(SpawnCategory)}
	 */
	@Override @Deprecated
	public int getAmbientSpawnLimit() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets user-specified limit for number of {@link SpawnCategory} mobs that can spawn in
	 * a chunk.
	 *
	 * <b>Note: the {@link SpawnCategory#MISC} are not consider.</b>
	 *
	 * @param spawnCategory the category spawn
	 * @return the {@link SpawnCategory} spawn limit
	 */
	@Override
	public int getSpawnLimit(@NonNull SpawnCategory spawnCategory) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Checks the current thread against the expected primary thread for the
	 * server.
	 * <p>
	 * <b>Note:</b> this method should not be used to indicate the current
	 * synchronized state of the runtime. A current thread matching the main
	 * thread indicates that it is synchronized, but a mismatch <b>does not
	 * preclude</b> the same assumption.
	 *
	 * @return true if the current thread matches the expected primary thread,
	 *     false otherwise
	 */
	@Override
	public boolean isPrimaryThread() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the message that is displayed on the server list.
	 *
	 * @return the servers MOTD
	 */
	@Override @NonNull
	public String getMotd() {
		return this.config.getMotd();
	}

	/**
	 * Set the message that is displayed on the server list.
	 *
	 * @param motd The message to be displayed
	 */
	@Override
	public void setMotd(@NonNull String motd) {
		this.config.setMotd(motd);
	}

	/**
	 * Gets the default message that is displayed when the server is stopped.
	 *
	 * @return the shutdown message
	 */
	@Override @Nullable
	public String getShutdownMessage() {
		return "Server closed";
	}

	/**
	 * Gets the current warning state for the server.
	 *
	 * @return the configured warning state
	 */
	@Override @NonNull
	public Warning.WarningState getWarningState() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the instance of the item factory (for {@link ItemMeta}).
	 *
	 * @return the item factory
	 * @see ItemFactory
	 */
	@Override @NonNull
	public ItemFactory getItemFactory() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the instance of the entity factory (for {@link EntitySnapshot}).
	 *
	 * @return the entity factory
	 * @see EntityFactory
	 */
	@Override @NonNull
	public EntityFactory getEntityFactory() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the instance of the scoreboard manager.
	 * <p>
	 * This will only exist after the first world has loaded.
	 *
	 * @return the scoreboard manager or null if no worlds are loaded.
	 */
	@Override @Nullable
	public ScoreboardManager getScoreboardManager() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get (or create) a new {@link Criteria} by its name.
	 *
	 * @param name the criteria name
	 * @return the criteria
	 * @see Criteria Criteria for a list of constants
	 */
	@Override @NonNull
	public Criteria getScoreboardCriteria(@NonNull String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets an instance of the server's default server-icon.
	 *
	 * @return the default server-icon; null values may be used by the
	 *     implementation to indicate no defined icon, but this behavior is
	 *     not guaranteed
	 */
	@Override @NonNull
	public CachedServerIcon getServerIcon() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Loads an image from a file, and returns a cached image for the specific
	 * server-icon.
	 * <p>
	 * Size and type are implementation defined. An incompatible file is
	 * guaranteed to throw an implementation-defined {@link Exception}.
	 *
	 * @param file the file to load the from
	 * @return a cached server-icon that can be used for a {@link
	 *     ServerListPingEvent#setServerIcon(CachedServerIcon)}
	 * @throws IllegalArgumentException if image is null
	 * @throws Exception if the image does not meet current server server-icon
	 *     specifications
	 */
	@Override @NonNull
	public CachedServerIcon loadServerIcon(@NonNull File file) throws IllegalArgumentException, Exception {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates a cached server-icon for the specific image.
	 * <p>
	 * Size and type are implementation defined. An incompatible file is
	 * guaranteed to throw an implementation-defined {@link Exception}.
	 *
	 * @param image the image to use
	 * @return a cached server-icon that can be used for a {@link
	 *     ServerListPingEvent#setServerIcon(CachedServerIcon)}
	 * @throws IllegalArgumentException if image is null
	 * @throws Exception if the image does not meet current server
	 *     server-icon specifications
	 */
	@Override @NonNull
	public CachedServerIcon loadServerIcon(@NonNull BufferedImage image) throws IllegalArgumentException, Exception {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Set the idle kick timeout. Any players idle for the specified amount of
	 * time will be automatically kicked.
	 * <p>
	 * A value of 0 will disable the idle kick timeout.
	 *
	 * @param threshold the idle timeout in minutes
	 */
	@Override
	public void setIdleTimeout(int threshold) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the idle kick timeout.
	 *
	 * @return the idle timeout in minutes
	 */
	@Override
	public int getIdleTimeout() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Create a ChunkData for use in a generator.
	 *
	 * See {@link ChunkGenerator#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}
	 *
	 * @param world the world to create the ChunkData for
	 * @return a new ChunkData for the world
	 *
	 */
	@Override @NonNull
	public ChunkGenerator.ChunkData createChunkData(@NonNull World world) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates a boss bar instance to display to players. The progress
	 * defaults to 1.0
	 *
	 * @param title the title of the boss bar
	 * @param color the color of the boss bar
	 * @param style the style of the boss bar
	 * @param flags an optional list of flags to set on the boss bar
	 * @return the created boss bar
	 */
	@Override @NonNull
	public BossBar createBossBar(@Nullable String title, @NonNull BarColor color,
	                             @NonNull BarStyle style, @NonNull BarFlag... flags) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates a boss bar instance to display to players. The progress defaults
	 * to 1.0.
	 * <br>
	 * This instance is added to the persistent storage of the server and will
	 * be editable by commands and restored after restart.
	 *
	 * @param key the key of the boss bar that is used to access the boss bar
	 * @param title the title of the boss bar
	 * @param color the color of the boss bar
	 * @param style the style of the boss bar
	 * @param flags an optional list of flags to set on the boss bar
	 * @return the created boss bar
	 */
	@Override @NonNull
	public KeyedBossBar createBossBar(@NonNull NamespacedKey key, @Nullable String title,
	                                  @NonNull BarColor color, @NonNull BarStyle style, @NonNull BarFlag... flags) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets an unmodifiable iterator through all persistent bossbars.
	 * <ul>
	 *   <li><b>not</b> bound to a {@link org.bukkit.entity.Boss}</li>
	 *   <li>
	 *     <b>not</b> created using
	 *     {@link #createBossBar(String, BarColor, BarStyle, BarFlag...)}
	 *   </li>
	 * </ul>
	 *
	 * e.g. bossbars created using the bossbar command
	 *
	 * @return a bossbar iterator
	 */
	@Override @NonNull
	public Iterator<KeyedBossBar> getBossBars() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the {@link KeyedBossBar} specified by this key.
	 * <ul>
	 *   <li><b>not</b> bound to a {@link org.bukkit.entity.Boss}</li>
	 *   <li>
	 *     <b>not</b> created using
	 *     {@link #createBossBar(String, BarColor, BarStyle, BarFlag...)}
	 *   </li>
	 * </ul>
	 *
	 * e.g. bossbars created using the bossbar command
	 *
	 * @param key unique bossbar key
	 * @return bossbar or null if not exists
	 */
	@Override @Nullable
	public KeyedBossBar getBossBar(@NonNull NamespacedKey key) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Removes a {@link KeyedBossBar} specified by this key.
	 * <ul>
	 *   <li><b>not</b> bound to a {@link org.bukkit.entity.Boss}</li>
	 *   <li>
	 *     <b>not</b> created using
	 *     {@link #createBossBar(String, BarColor, BarStyle, BarFlag...)}
	 *   </li>
	 * </ul>
	 *
	 * e.g. bossbars created using the bossbar command
	 *
	 * @param key unique bossbar key
	 * @return true if removal succeeded or false
	 */
	@Override
	public boolean removeBossBar(@NonNull NamespacedKey key) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets an entity on the server by its UUID
	 *
	 * @param uuid the UUID of the entity
	 * @return the entity with the given UUID, or null if it isn't found
	 */
	@Override @Nullable
	public Entity getEntity(@NonNull UUID uuid) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get the advancement specified by this key.
	 *
	 * @param key unique advancement key
	 * @return advancement or null if not exists
	 */
	@Override @Nullable
	public Advancement getAdvancement(@NonNull NamespacedKey key) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Get an iterator through all advancements. Advancements cannot be removed
	 * from this iterator,
	 *
	 * @return an advancement iterator
	 */
	@Override @NonNull
	public Iterator<Advancement> advancementIterator() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates a new {@link BlockData} instance for the specified Material, with
	 * all properties initialized to unspecified defaults.
	 *
	 * @param material the material
	 * @return new data instance
	 */
	@Override @NonNull
	public BlockData createBlockData(@NonNull Material material) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates a new {@link BlockData} instance for the specified Material, with
	 * all properties initialized to unspecified defaults.
	 *
	 * @param material the material
	 * @param consumer consumer to run on new instance before returning
	 * @return new data instance
	 */
	@Override @NonNull
	public BlockData createBlockData(@NonNull Material material, @Nullable Consumer<? super BlockData> consumer) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates a new {@link BlockData} instance with material and properties
	 * parsed from provided data.
	 *
	 * @param data data string
	 * @return new data instance
	 * @throws IllegalArgumentException if the specified data is not valid
	 */
	@Override @NonNull
	public BlockData createBlockData(@NonNull String data) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Creates a new {@link BlockData} instance for the specified Material, with
	 * all properties initialized to unspecified defaults, except for those
	 * provided in data.
	 * <br>
	 * If <code>material</code> is specified, then the data string must not also
	 * contain the material.
	 *
	 * @param material the material
	 * @param data data string
	 * @return new data instance
	 * @throws IllegalArgumentException if the specified data is not valid
	 */
	@Override @NonNull
	public BlockData createBlockData(@Nullable Material material,
	                                 @Nullable String data) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a tag which has already been defined within the server. Plugins are
	 * suggested to use the concrete tags in {@link Tag} rather than this method
	 * which makes no guarantees about which tags are available, and may also be
	 * less performant due to lack of caching.
	 * <br>
	 * Tags will be searched for in an implementation specific manner, but a
	 * path consisting of namespace/tags/registry/key is expected.
	 * <br>
	 * Server implementations are allowed to handle only the registries
	 * indicated in {@link Tag}.
	 *
	 * @param <T> type of the tag
	 * @param registry the tag registry to look at
	 * @param tag the name of the tag
	 * @param clazz the class of the tag entries
	 * @return the tag or null
	 */
	@Override @Nullable
	public <T extends Keyed> Tag<T> getTag(@NonNull String registry,
	                                       @NonNull NamespacedKey tag, @NonNull Class<T> clazz) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a all tags which have been defined within the server.
	 * <br>
	 * Server implementations are allowed to handle only the registries
	 * indicated in {@link Tag}.
	 * <br>
	 * No guarantees are made about the mutability of the returned iterator.
	 *
	 * @param <T> type of the tag
	 * @param registry the tag registry to look at
	 * @param clazz the class of the tag entries
	 * @return all defined tags
	 */
	@Override @NonNull
	public <T extends Keyed> Iterable<Tag<T>> getTags(@NonNull String registry, @NonNull Class<T> clazz) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the specified {@link LootTable}.
	 *
	 * @param key the name of the LootTable
	 * @return the LootTable, or null if no LootTable is found with that name
	 */
	@Override @Nullable
	public LootTable getLootTable(@NonNull NamespacedKey key) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Selects entities using the given Vanilla selector.
	 * <br>
	 * No guarantees are made about the selector format, other than they match
	 * the Vanilla format for the active Minecraft version.
	 * <br>
	 * Usually a selector will start with '@', unless selecting a Player in
	 * which case it may simply be the Player's name or UUID.
	 * <br>
	 * Note that in Vanilla, elevated permissions are usually required to use
	 * '@' selectors, but this method should not check such permissions from the
	 * sender.
	 *
	 * @param sender the sender to execute as, must be provided
	 * @param selector the selection string
	 * @return a list of the selected entities. The list will not be null, but
	 * no further guarantees are made.
	 * @throws IllegalArgumentException if the selector is malformed in any way
	 * or a parameter is null
	 */
	@Override @NonNull
	public List<Entity> selectEntities(@NonNull CommandSender sender,
	                                   @NonNull String selector) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets the structure manager for loading and saving structures.
	 *
	 * @return the structure manager
	 */
	@Override @NonNull
	public StructureManager getStructureManager() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Returns the registry for the given class.
	 * <br>
	 * If no registry is present for the given class null will be returned.
	 * <br>
	 * Depending on the implementation not every registry present in
	 * {@link Registry} will be returned by this method.
	 *
	 * @param tClass of the registry to get
	 * @param <T> type of the registry
	 * @return the corresponding registry or null if not present
	 */
	@Override @Nullable
	public <T extends Keyed> Registry<T> getRegistry(@NonNull Class<T> tClass) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * @return the unsafe values instance
	 * @see UnsafeValues
	 */
	@Override @Deprecated @NonNull
	public UnsafeValues getUnsafe() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * @return Additional Spigot APIs
	 */
	@Override
	public Spigot spigot() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Sends this recipient a Plugin Message on the specified outgoing
	 * channel.
	 * <p>
	 * The message may not be larger than {@link Messenger#MAX_MESSAGE_SIZE}
	 * bytes, and the plugin must be registered to send messages on the
	 * specified channel.
	 *
	 * @param source The plugin that sent this message.
	 * @param channel The channel to send this message on.
	 * @param message The raw message to send.
	 * @throws IllegalArgumentException Thrown if the source plugin is
	 *     disabled.
	 * @throws IllegalArgumentException Thrown if source, channel or message
	 *     is null.
	 * @throws MessageTooLargeException Thrown if the message is too big.
	 * @throws ChannelNotRegisteredException Thrown if the channel is not
	 *     registered for this plugin.
	 */
	@Override
	public void sendPluginMessage(@NonNull Plugin source, @NonNull String channel, @NonNull byte[] message) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Gets a set containing all the Plugin Channels that this client is
	 * listening on.
	 *
	 * @return Set containing all the channels that this client may accept.
	 */
	@Override @NonNull
	public Set<String> getListeningPluginChannels() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
