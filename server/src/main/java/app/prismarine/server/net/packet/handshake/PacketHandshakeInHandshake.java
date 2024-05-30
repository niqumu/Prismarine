package app.prismarine.server.net.packet.handshake;

import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class PacketHandshakeInHandshake implements Packet {

	private final int protocolVersion;
	private final String serverAddress;
	private final int serverPort;
	private final HandshakeState nextState;

	public PacketHandshakeInHandshake(ByteBufWrapper bytes) {
		this.protocolVersion = bytes.readVarInt();
		this.serverAddress = bytes.readString();
		this.serverPort = bytes.readShort() & 0xffff;
		this.nextState = HandshakeState.fromID(bytes.readVarInt());
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
		return ConnectionState.HANDSHAKING;
	}

	/**
	 * @return The internal ID of the packet
	 */
	@Override
	public int getID() {
		return 0;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeInt(this.protocolVersion);
		bytes.writeString(this.serverAddress);
		bytes.writeShort(this.serverPort);
		bytes.writeVarInt(this.nextState.getId());
		return bytes.getBytes();
	}

	@Getter
	@AllArgsConstructor
	public enum HandshakeState {
		STATUS(1),
		LOGIN(2);

		private final int id;

		public static HandshakeState fromID(int id) {
			if (id == 1) {
				return STATUS;
			} else if (id == 2) {
				return LOGIN;
			}

			throw new IllegalArgumentException(id + " isn't a valid handshake state!");
		}
	}
}
