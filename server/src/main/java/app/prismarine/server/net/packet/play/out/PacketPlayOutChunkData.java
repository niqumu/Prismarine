package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Chunk;

@Data
@AllArgsConstructor
public class PacketPlayOutChunkData implements Packet {

	private final Chunk chunk;

	public PacketPlayOutChunkData(ByteBufWrapper bytes) {
		throw new UnsupportedOperationException("Attempting to decode outbound packet!");
	}

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

		return bytes.getBytes();
	}
}
