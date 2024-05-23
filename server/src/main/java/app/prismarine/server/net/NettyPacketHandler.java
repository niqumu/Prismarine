package app.prismarine.server.net;

import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NettyPacketHandler extends ChannelInboundHandlerAdapter {

	private final Connection connection;

	// Called when a packet is received
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		PacketManager.handle(this.connection, (Packet) msg);
	}
}
