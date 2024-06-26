package app.prismarine.server.net;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.ServerConfig;
import app.prismarine.server.entity.PrismarineEntity;
import app.prismarine.server.entity.PrismarinePlayer;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.net.packet.configuration.PacketConfigurationOutDisconnect;
import app.prismarine.server.net.packet.login.PacketLoginOutDisconnect;
import app.prismarine.server.net.packet.play.out.*;
import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.util.MojangUtil;
import app.prismarine.server.world.PrismarineChunk;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * A remote connection to the server, which may or may not belong to a player
 */
@Getter
@Setter
public class Connection {

	private static final long KEEP_ALIVE_FREQUENCY = 10000;

	/**
	 * The {@link ConnectionState} that this connection is currently in
	 */
	private ConnectionState state = ConnectionState.HANDSHAKING;

	/**
	 * The size, in bytes, for a packet to be before it is compressed
	 */
	private int compressionThreshold = 0;

	/**
	 * The {@link NettyServer} the remote client is connected to
	 */
	private final NettyServer nettyServer;

	/**
	 * The channel that the remote client is connected to the server over
	 */
	private final Channel channel;

	/**
	 * The remote address of the client
	 */
	private final InetSocketAddress address;

	/**
	 * The protocol version of the client
	 */
	private int protocolVersion;

	/**
	 * The latency of the connection
	 */
	private int latency;

	/**
	 * The player associated with the connection, or null if none exists
	 */
	@Nullable
	private Player player;

	/**
	 * The time at which a keep alive packet was last sent to the client
	 */
	private long lastKeepAliveTime = System.currentTimeMillis();

	private boolean finishedLogin = false;

	/**
	 * Create a new client-server connection based on the netty server and channel
	 * @param nettyServer The netty server the client is connected to
	 * @param channel The netty channel the client is connected via
	 */
	public Connection(@NotNull NettyServer nettyServer, @NotNull Channel channel) {
		this.nettyServer = nettyServer;
		this.channel = channel;
		this.address = (InetSocketAddress) channel.remoteAddress();
	}

	/**
	 * Called when the client attempts to log into the server
	 * @param profile The profile of the client connecting
	 */
	public void login(PlayerProfile profile) {

		// Ensure that this connection doesn't already have a player associated with it
		if (this.player != null) {
			throw new IllegalStateException("Connection already has a player assigned to it!");
		}

		// Kick all other connections from the same UUID
		Bukkit.getServer().getOnlinePlayers().forEach(player -> {
			if (player.getUniqueId().equals(profile.getUniqueId())) {
				player.kickPlayer("You logged in from another location!");
			}
		});

		// Create the player
		Location spawnLocation = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
		this.player = new PrismarinePlayer(Bukkit.getServer(), spawnLocation, profile, this);
	}

	/**
	 * Called when the configuration stage is finished and the connection is switched to PLAY
	 */
	public void finishLogin() {
		this.setState(ConnectionState.PLAY);

		// Send login packet
		this.sendPacket(new PacketPlayOutLogin(this.player));

		// Register the player with the world
		this.player.getWorld().addEntity(this.player);

		// Register the player with the server's entity manager
		((PrismarineServer) Bukkit.getServer()).getEntityManager().register((PrismarineEntity) this.player);

		// Set the player position
		Location spawnLocation = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
		this.player.teleport(spawnLocation);

		// Fetch player textures
		String textures = MojangUtil.fetchTextures(this.player);

		// Broadcast connecting player info
		Packet addPlayer = new PacketPlayOutPlayerInfoUpdate(this.player.getUniqueId(),
			new PacketPlayOutPlayerInfoUpdate.ActionAddPlayer(this.player.getName(), textures));
		this.nettyServer.broadcastPacket(addPlayer);
		Packet showPlayer = new PacketPlayOutPlayerInfoUpdate(this.player.getUniqueId(),
			new PacketPlayOutPlayerInfoUpdate.ActionUpdateListed(true));
		this.nettyServer.broadcastPacket(showPlayer);

		// Send the connecting player the player info of all currently online players
		this.nettyServer.getServer().getOnlinePlayers().forEach(player -> {
			String existingTextures = MojangUtil.fetchTextures(player);

			Packet addExistingPlayer = new PacketPlayOutPlayerInfoUpdate(player.getUniqueId(),
				new PacketPlayOutPlayerInfoUpdate.ActionAddPlayer(player.getName(), existingTextures));
			this.sendPacket(addExistingPlayer);
			Packet showExistingPlayer = new PacketPlayOutPlayerInfoUpdate(player.getUniqueId(),
				new PacketPlayOutPlayerInfoUpdate.ActionUpdateListed(true));
			this.sendPacket(showExistingPlayer);
		});

		// Create and fire a new player join event
		PlayerJoinEvent event = ((PrismarineServer) Bukkit.getServer()).getEventManager().onPlayerJoin(this.player);

		// Broadcast the join message if one exists
		if (event.getJoinMessage() != null) {
			Bukkit.getServer().broadcastMessage(event.getJoinMessage());
		}

		// Prepare for the player's world to send chunks
		this.sendPacket(new PacketPlayOutGameEvent(PacketPlayOutGameEvent.Event.START_WAITING_FOR_CHUNKS, 0));

		// TODO testing
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				this.sendPacket(new PacketPlayOutChunkData(new PrismarineChunk(x, z, null)));
			}
		}

		this.finishedLogin = true;
	}

	/**
	 * Abnormally disconnects the client
	 * @param reason The reason provided for the kick
	 */
	public void disconnect(String reason) {

		if (this.player != null) {

			// Create and fire a new player kick event
			PlayerKickEvent playerKickEvent = ((PrismarineServer) Bukkit.getServer()).
				getEventManager().onPlayerKick(this.player, reason);

			// Update the reason
			reason = playerKickEvent.getReason();

			// Broadcast the quit message
			Bukkit.getServer().broadcastMessage(playerKickEvent.getLeaveMessage());
		}

		PrismarineServer.LOGGER.info("{} lost connection: {}", this.getName(), reason);

		switch (this.state) {
			case LOGIN -> this.sendPacket(new PacketLoginOutDisconnect(reason));
			case CONFIGURATION -> this.sendPacket(new PacketConfigurationOutDisconnect(reason));
			case PLAY -> this.sendPacket(new PacketPlayOutDisconnect(reason));
		}

		this.close();
	}

	public void tick() {

		// Keep alive (play)
		if (this.state.equals(ConnectionState.PLAY)) {
			if (System.currentTimeMillis() - this.lastKeepAliveTime > KEEP_ALIVE_FREQUENCY) {
				this.sendPacket(new PacketPlayOutKeepAlive());
				this.lastKeepAliveTime = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Sends a packet to a client over this connection
	 * @param packet The packet to send to the client
	 * @return The {@link ChannelFuture} of the operation
	 */
	public ChannelFuture sendPacket(@NotNull Packet packet) {
		if (!this.channel.isOpen()) {
			throw new IllegalStateException("Cannot write to a closed channel!");
		}

		if (packet.getDirection().equals(PacketDirection.IN)) {
			throw new IllegalArgumentException("Cannot send an inbound packet!");
		}

		return this.channel.writeAndFlush(packet);
	}

	/**
	 * Sends a bundle of packets to a client over this connection
	 * @param packets The packets to send to the client
	 */
	public void sendPacketBundle(@NotNull Packet... packets) {
		if (!this.channel.isOpen()) {
			throw new IllegalStateException("Cannot write to a closed channel!");
		}

		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeBytes(new PacketPlayOutDelimiter().serializeRaw());
		Arrays.stream(packets).forEach(packet -> bytes.writeBytes(packet.serializeRaw()));
		bytes.writeBytes(new PacketPlayOutDelimiter().serializeRaw());

		this.channel.writeAndFlush(bytes.getBytes());
	}

	/**
	 * Closes and unregisters the connection
	 */
	public void close() {
		this.getChannel().close();
		this.onClose();
	}

	/**
	 * Called whenever the connection is closed by either the client or the server
	 */
	public void onClose() {
		this.nettyServer.getConnections().remove(this);

		if (this.player != null) {

			// Remove the player from the global entity manager and the player's world
			((PrismarineServer) Bukkit.getServer()).getEntityManager().free((PrismarineEntity) this.player);
			this.player.getWorld().getPlayers().remove(this.player);
			this.player.getWorld().getEntities().remove(this.player);

			// Create and fire a new player quit event
			PlayerQuitEvent playerQuitEvent = ((PrismarineServer) Bukkit.getServer()).
				getEventManager().onPlayerQuit(this.player);

			// Broadcast the quit message if one exists
			if (playerQuitEvent.getQuitMessage() != null) {
				Bukkit.getServer().broadcastMessage(playerQuitEvent.getQuitMessage());
			}
		}
	}

	/**
	 * Gets the most appropriate String representation of this Connection given its state
	 * @return If this connection has a player associated with it, the player name, otherwise the remote address if
	 *         permitted. If printing the address is not allowed ({@link ServerConfig#isLogIPs()}, a * is returned.
	 */
	public String getName() {

		// Return the name if possible
		if (this.player != null) {
			return this.player.getName();
		}

		// Return the remote address if possible
		if (this.nettyServer.getServer().isLoggingIPs()) {
			return this.getAddress().toString();
		}

		return "*"; // Fallback
	}
}
