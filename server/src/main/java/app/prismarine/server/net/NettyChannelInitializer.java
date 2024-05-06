package app.prismarine.server.net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final NettyServer nettyServer;

	public NettyChannelInitializer(NettyServer nettyServer) {
		this.nettyServer = nettyServer;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		Connection connection = new Connection(nettyServer, socketChannel);

		socketChannel.config().setKeepAlive(true);
		socketChannel.pipeline().addLast(
			new PacketDecoder(connection),
			new PacketEncoder(connection),
			new NettyPacketHandler(connection)
		);

		this.nettyServer.getConnections().add(connection);
	}
}
