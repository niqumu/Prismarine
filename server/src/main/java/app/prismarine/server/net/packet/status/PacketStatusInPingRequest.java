package app.prismarine.server.net.packet.status;

import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PacketStatusInPingRequest implements Packet {

	private final long payload;

	public PacketStatusInPingRequest(ByteBufWrapper bytes) {
		this.payload = bytes.readLong();
	}

	/**
	 * @return The direction of the packet - either client -> server (in), or vice versa
	 * @see PacketDirection
	 */
	@Override
	public PacketDirection getDirection() {
		return PacketDirection.IN;
	}

	/**
	 * @return The ConnectionState that this packet is sent during
	 * @see ConnectionState
	 */
	@Override
	public ConnectionState getState() {
		return ConnectionState.STATUS;
	}

	/**
	 * @return The internal ID of the packet
	 */
	@Override
	public int getID() {
		return 1;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeLong(this.payload);
		return bytes.getBytes();
	}
}
