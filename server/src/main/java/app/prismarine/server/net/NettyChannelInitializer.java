package app.prismarine.server.net;

import app.prismarine.server.net.packet.PacketDecoder;
import app.prismarine.server.net.packet.PacketEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final NettyServer nettyServer;

	public NettyChannelInitializer(NettyServer nettyServer) {
		this.nettyServer = nettyServer;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) {
		Connection connection = new Connection(nettyServer, socketChannel);

		socketChannel.config().setKeepAlive(true);
		socketChannel.pipeline().addLast(
			new PacketDecoder(connection),
			new PacketEncoder(connection),
			new ByteArrayEncoder(),
			new NettyPacketHandler(connection),
			new NettyInactiveHandler(connection)
		);

		this.nettyServer.getConnections().add(connection);
	}
}
