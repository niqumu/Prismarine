package app.prismarine.server.net.packet;

import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.util.ByteBufWrapper;

/**
 * Represents an abstract packet between the client and server
 */
public interface Packet {

	/**
	 * @return The direction of the packet - either client -> server (in), or vice versa
	 * @see PacketDirection
	 */
	PacketDirection getDirection();

	/**
	 * @return The ConnectionState that this packet is sent during
	 * @see ConnectionState
	 */
	ConnectionState getState();

	/**
	 * @return The internal ID of the packet
	 */
	int getID();

	/**
	 * @return The packet in raw, serialized form
	 */
	byte[] serialize();

	/**
	 * @return The packet in raw, serialized form, including the packet ID and length
	 */
	default byte[] serializeRaw() {
		byte[] serializedPacket = this.serialize();

		ByteBufWrapper idBuffer = new ByteBufWrapper();
		idBuffer.writeVarInt(this.getID());
		byte[] idBytes = idBuffer.getBytes();

		ByteBufWrapper wrappedOut = new ByteBufWrapper();

		wrappedOut.writeVarInt(serializedPacket.length + idBytes.length);
		wrappedOut.writeBytes(idBytes);
		wrappedOut.writeBytes(serializedPacket);

		return wrappedOut.getBytes();
	}
}
