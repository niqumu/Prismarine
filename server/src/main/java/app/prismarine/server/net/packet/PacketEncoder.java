package app.prismarine.server.net.packet;

import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.NettyServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PacketEncoder extends MessageToByteEncoder<Packet> {

	private final Connection connection;

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet in, ByteBuf out) {

		if (NettyServer.ENABLE_PACKET_LOGGING) {
			System.out.println("(" + connection.getAddress() + "/OUT): " + in.toString());
		}

		out.writeBytes(in.serializeRaw());
	}
}
