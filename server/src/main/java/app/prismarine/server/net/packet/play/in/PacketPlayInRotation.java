package app.prismarine.server.net.packet.play.in;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.Data;

@Data
public class PacketPlayInRotation implements Packet {

	private final float yaw, pitch;
	private final boolean onGround;

	public PacketPlayInRotation(ByteBufWrapper bytes) {
		this.yaw = bytes.readFloat();
		this.pitch = bytes.readFloat();
		this.onGround = bytes.readBoolean();
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
		return 0x1c;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeFloat(this.yaw);
		bytes.writeFloat(this.pitch);
		bytes.writeBoolean(this.onGround);
		return bytes.getBytes();
	}
}
