package app.prismarine.server.net;

import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.net.packet.PacketManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Channel handler to decode the client's raw data into legible packets
 */
@RequiredArgsConstructor
public class PacketDecoder extends ByteToMessageDecoder {

	private final Connection connection;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		ByteBufWrapper wrapper = new ByteBufWrapper(in);

		ConnectionState state = this.connection.getState();
		int length = wrapper.readVarInt();
		int id = wrapper.readVarInt();

		Class<? extends Packet> packetClass = PacketManager.match(state, PacketDirection.IN, id);

		if (packetClass == null) {
			System.out.println("Got unknown packet.");
			System.out.println("\tState: " + state);
			System.out.println("\tID: " + id);
			in.clear();
			return;
		}

		Packet packet = PacketManager.build(packetClass, wrapper);

		System.out.println("IN: " + packet.toString());

		out.add(packet);
	}
}
