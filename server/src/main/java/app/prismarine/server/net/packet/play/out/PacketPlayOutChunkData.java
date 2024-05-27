package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.world.PrismarineChunk;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PacketPlayOutChunkData implements Packet {

	private final PrismarineChunk chunk;

	/**
	 * @return The direction of the packet - either client -> server (in), or vice versa
	 * @see PacketDirection
	 */
	@Override
	public PacketDirection getDirection() {
		return PacketDirection.OUT;
	}

	/**
	 * @return The ConnectionState that this packet is sent during
	 * @see ConnectionState
	 */
	@Override
	public ConnectionState getState() {
		return ConnectionState.PLAY;
	}

	/**
	 * @return The internal ID of the packet
	 */
	@Override
	public int getID() {
		return 0x27;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();

		bytes.writeInt(this.chunk.getX());
		bytes.writeInt(this.chunk.getZ());
//		bytes.writeInt(x);
//		bytes.writeInt(z);

		// Heightmap
		bytes.writeBytes(new byte[]{0x0a, 0x00});

		// Chunk sections
		byte[] data = this.chunk.getSerializedData();
		bytes.writeVarInt(data.length);
		bytes.writeBytes(data);

		bytes.writeVarInt(0);
		bytes.writeVarInt(0);
		bytes.writeVarInt(0);
		bytes.writeVarInt(0);
		bytes.writeVarInt(0);
		bytes.writeVarInt(0);
		bytes.writeVarInt(0);

		return bytes.getBytes();
	}
}
