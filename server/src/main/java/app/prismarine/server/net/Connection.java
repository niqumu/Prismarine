package app.prismarine.server.net;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.net.InetSocketAddress;

@Getter
public class Connection {

	@Getter @Setter
	private ConnectionState state = ConnectionState.HANDSHAKING;

	@Getter @Setter
	private int compressionThreshold = 0;

	@Getter
	private final NettyServer nettyServer;

	@Getter
	private final Channel channel;

	@Getter
	private final InetSocketAddress address;

	@Getter
	private final PrismarineServer server;

	public Connection(@NonNull NettyServer nettyServer, @NonNull Channel channel) {
		this.nettyServer = nettyServer;
		this.channel = channel;
		this.address = (InetSocketAddress) channel.remoteAddress();

		this.server = PrismarineServer.getServer();
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
}
