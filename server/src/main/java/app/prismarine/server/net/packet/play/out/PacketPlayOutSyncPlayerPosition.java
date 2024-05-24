package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;

@Data
public class PacketPlayOutSyncPlayerPosition implements Packet {

	private double x, y, z;
	private float yaw, pitch;
	private byte flags;

	public PacketPlayOutSyncPlayerPosition(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.flags = 0b11111;
	}

	public PacketPlayOutSyncPlayerPosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.flags = 0b00111;
	}

	public PacketPlayOutSyncPlayerPosition(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.flags = 0b11000;
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
		return 0x40;
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
		bytes.writeByte(this.flags);
		bytes.writeVarInt((new Random()).nextInt());

		return bytes.getBytes();
	}
}
