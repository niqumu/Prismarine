package app.prismarine.server.net.packet;

import app.prismarine.server.net.ConnectionState;

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
}
