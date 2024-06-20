package app.prismarine.server.net.packet;

import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.NettyServer;
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
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		ByteBufWrapper copy = new ByteBufWrapper(in.copy());
		ByteBufWrapper wrapper = new ByteBufWrapper(in);

		ConnectionState state = this.connection.getState();
		int length = wrapper.readVarInt();
		int id = wrapper.readVarInt();

		Class<? extends Packet> packetClass = PacketManager.match(state, PacketDirection.IN, id);

		// If the packet could not be resolved
		if (packetClass == null) {
//			PrismarineServer.LOGGER.warn("Received an unknown packet from {}", this.connection.getName());
//			PrismarineServer.LOGGER.warn("Connection state: {}, Packet ID: {}", this.connection.getState(), id);
//			PrismarineServer.LOGGER.warn("Packet dump: {}", Arrays.toString(copy.getBytes()));
			in.clear();
			return;
		}

		Packet packet = PacketManager.build(packetClass, wrapper);
		out.add(packet);

		if (NettyServer.ENABLE_PACKET_LOGGING) {
			System.out.println("(" + connection.getAddress() + "/IN): " + packet.toString());
		}

		copy.getByteBuf().release();
	}
}
