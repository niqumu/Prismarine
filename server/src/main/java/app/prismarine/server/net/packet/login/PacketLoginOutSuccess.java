package app.prismarine.server.net.packet.login;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PacketLoginOutSuccess implements Packet {

	private final UUID uuid;
	private final String name;

	public PacketLoginOutSuccess(ByteBufWrapper bytes) {
		this.uuid = bytes.readUUID();
		this.name = bytes.readString();
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
		return ConnectionState.LOGIN;
	}

	/**
	 * @return The internal ID of the packet
	 */
	@Override
	public int getID() {
		return 0x2;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeUUID(this.uuid);
		bytes.writeString(this.name);

		// TODO, testing hardcode
		bytes.writeByte(0);
		bytes.writeVarInt(0);
		return bytes.getBytes();
	}
}
