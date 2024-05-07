package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PacketPlayOutLogin implements Packet {

	public PacketPlayOutLogin(ByteBufWrapper bytes) {
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
		return 0x29;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeInt(0);
		bytes.writeBoolean(false);
		bytes.writeVarInt(1);
		bytes.writeIdentifier("minecraft", "overworld");
		bytes.writeVarInt(20);
		bytes.writeVarInt(16);
		bytes.writeVarInt(16);
		bytes.writeBoolean(false);
		bytes.writeBoolean(true);
		bytes.writeBoolean(false);
		bytes.writeIdentifier("minecraft", "overworld");
		bytes.writeIdentifier("minecraft", "overworld");
		bytes.writeLong(0);
		bytes.writeByte(1);
		bytes.writeBoolean(false);
		bytes.writeBoolean(true);
		bytes.writeBoolean(false);
		bytes.writeVarInt(0);
		return bytes.getBytes();
	}
}
