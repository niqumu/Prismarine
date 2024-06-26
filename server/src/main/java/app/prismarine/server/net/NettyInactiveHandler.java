package app.prismarine.server.net;

import app.prismarine.server.PrismarineServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NettyInactiveHandler extends ChannelInboundHandlerAdapter {

	private final Connection connection;

	// Called when the connection is closed
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

		// If the connection is being terminated by the client
		if (connection.getNettyServer().getConnections().contains(connection)) {
			PrismarineServer.LOGGER.info("{} lost connection: Disconnected", connection.getName());
		}

		connection.onClose();

		super.channelInactive(ctx);
	}
}
