package app.prismarine.server.net.packet;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.Connection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PacketEncoder extends MessageToByteEncoder<Packet> {

	private final Connection connection;

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet in, ByteBuf out) {

		System.out.println("(" + connection.getAddress() + "/OUT): " + in.toString());

		byte[] serializedPacket = in.serialize();

		ByteBufWrapper idBuffer = new ByteBufWrapper();
		idBuffer.writeVarInt(in.getID());
		byte[] idBytes = idBuffer.getBytes();

		ByteBufWrapper wrappedOut = new ByteBufWrapper();

		wrappedOut.writeVarInt(serializedPacket.length + idBytes.length);
		wrappedOut.writeBytes(idBytes);
		wrappedOut.writeBytes(serializedPacket);

		out.writeBytes(wrappedOut.getBytes());
	}
}
