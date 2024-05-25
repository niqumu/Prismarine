package app.prismarine.server.net;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.entity.PrismarineEntity;
import app.prismarine.server.entity.PrismarinePlayer;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.net.packet.configuration.PacketConfigurationOutDisconnect;
import app.prismarine.server.net.packet.login.PacketLoginOutDisconnect;
import app.prismarine.server.net.packet.play.out.PacketPlayOutDisconnect;
import app.prismarine.server.net.packet.play.out.PacketPlayOutKeepAlive;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * A remote connection to the server, which may or may not belong to a player
 */
@Getter
public class Connection {

	private static final long KEEP_ALIVE_FREQUENCY = 10000;

	/**
	 * The {@link ConnectionState} that this connection is currently in
	 */
	@Getter @Setter
	private ConnectionState state = ConnectionState.HANDSHAKING;

	/**
	 * The size, in bytes, for a packet to be before it is compressed
	 */
	@Getter @Setter
	private int compressionThreshold = 0;

	/**
	 * The {@link NettyServer} the remote client is connected to
	 */
	@Getter
	private final NettyServer nettyServer;

	/**
	 * The channel that the remote client is connected to the server over
	 */
	@Getter
	private final Channel channel;

	/**
	 * The remote address of the client
	 */
	@Getter
	private final InetSocketAddress address;

	/**
	 * The protocol version of the client
	 */
	@Getter @Setter
	private int protocolVersion;

	/**
	 * The latency of the connection
	 */
	@Getter @Setter
	private int latency;

	/**
	 * The player associated with the connection, or null if none exists
	 */
	@Getter @Setter
	private Player player;

	/**
	 * The time at which a keep alive packet was last sent to the client
	 */
	private long lastKeepAliveTime = System.currentTimeMillis();


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

		// Send the join message, TODO event
//		String joinMessage = ChatColor.YELLOW + profile.getName() + " joined the game";
//		Bukkit.getServer().broadcastMessage(joinMessage);

		// Create the player
//		Location spawnLocation = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
		Location spawnLocation = new Location(null, 0, 64, 0);
		this.player = new PrismarinePlayer(Bukkit.getServer(), spawnLocation, profile, this);

		// Register the player with the server's entity manager
		((PrismarineServer) Bukkit.getServer()).getEntityManager().register((PrismarineEntity) this.player);
	}

	/**
	 * Abnormally disconnects the client
	 * @param reason The reason provided for the kick
	 */
	public void disconnect(String reason) {

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
			((PrismarineServer) Bukkit.getServer()).getEntityManager().free((PrismarineEntity) this.player);
		}
	}

	/**
	 * Gets the most appropriate String representation of this Connection given its state
	 * @return If this connection has a player associated with it, the player name, otherwise the remote address.
	 */
	public String getName() {
		if (this.player != null) {
			return this.player.getName();
		}

		return this.getAddress().toString();
	}
}
