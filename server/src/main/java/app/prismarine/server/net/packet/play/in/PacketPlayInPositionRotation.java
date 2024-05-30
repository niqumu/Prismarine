package app.prismarine.server.net.packet.play.in;

import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.Data;

@Data
public class PacketPlayInPositionRotation implements Packet {

	private final double x, y, z;
	private final float yaw, pitch;
	private final boolean onGround;

	public PacketPlayInPositionRotation(ByteBufWrapper bytes) {
		this.x = bytes.readDouble();
		this.y = bytes.readDouble();
		this.z = bytes.readDouble();
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
		return 0x1b;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeDouble(this.x);
		bytes.writeDouble(this.y);
		bytes.writeDouble(this.z);
		bytes.writeFloat(this.yaw);
		bytes.writeFloat(this.pitch);
		bytes.writeBoolean(this.onGround);
		return bytes.getBytes();
	}
}
