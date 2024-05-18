package app.prismarine.server.net;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.net.packet.configuration.PacketConfigurationOutDisconnect;
import app.prismarine.server.net.packet.login.PacketLoginOutDisconnect;
import app.prismarine.server.net.packet.play.out.PacketPlayOutDisconnect;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.profile.PlayerProfile;

import java.net.InetSocketAddress;

/**
 * A remote connection to the server, which may or may not belong to a player
 */
@Getter
public class Connection {

	/**
	 * The {@link ConnectionState} that this connection is currently in
	 */
	@Getter @Setter
	private ConnectionState state = ConnectionState.HANDSHAKING;

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
	 * The profile of the client, or null if it has not yet been assigned
	 */
	@Getter @Setter
	private PlayerProfile profile;


	/**
	 * Create a new client-server connection based on the netty server and channel
	 * @param nettyServer The netty server the client is connected to
	 * @param channel The netty channel the client is connected via
	 */
	public Connection(@NonNull NettyServer nettyServer, @NonNull Channel channel) {
		this.nettyServer = nettyServer;
		this.channel = channel;
		this.address = (InetSocketAddress) channel.remoteAddress();
	}

	/**
	 * Abnormally disconnects the client
	 * @param reason The reason provided for the kick
	 */
	public void disconnect(String reason) {
		PrismarineServer.LOGGER.info("{} was kicked: {}", address, reason);

		switch (this.state) {
			case LOGIN -> this.sendPacket(new PacketLoginOutDisconnect(reason));
			case CONFIGURATION -> this.sendPacket(new PacketConfigurationOutDisconnect(reason));
			case PLAY -> this.sendPacket(new PacketPlayOutDisconnect(reason));
		}

		this.close();
	}

	public ChannelFuture sendPacket(@NonNull Packet packet) {
		if (!this.channel.isOpen()) {
			throw new IllegalStateException("Cannot write to a closed channel!");
		}

		if (packet.getDirection().equals(PacketDirection.IN)) {
			throw new IllegalArgumentException("Cannot send an inbound packet!");
		}

		return this.channel.writeAndFlush(packet);
	}

	public void close() {
		this.getChannel().close();
	}
}
