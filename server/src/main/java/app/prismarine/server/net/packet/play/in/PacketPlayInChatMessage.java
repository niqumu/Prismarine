package app.prismarine.server.net.packet.play.in;

import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.Data;

@Data
public class PacketPlayInChatMessage implements Packet {

	private final String message;
	private final long timestamp;
	private final long salt;

	private final boolean hasSignature;
	private final byte[] signature;

	public PacketPlayInChatMessage(ByteBufWrapper bytes) {
		this.message = bytes.readString();
		this.timestamp = bytes.readLong();
		this.salt = bytes.readLong();
		this.hasSignature = bytes.readBoolean();

		if (this.hasSignature) {
			this.signature = bytes.readBytes(256);
		} else {
			this.signature = new byte[]{};
		}
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
		return ConnectionState.PLAY;
	}

	/**
	 * @return The internal ID of the packet
	 */
	@Override
	public int getID() {
		return 0x6;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		return new byte[0]; // todo
	}
}
