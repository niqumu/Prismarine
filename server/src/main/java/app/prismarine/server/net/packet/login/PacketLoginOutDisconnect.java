package app.prismarine.server.net.packet.login;

import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.Data;

@Data
public class PacketLoginOutDisconnect implements Packet {

	private final String jsonReason;

	public PacketLoginOutDisconnect(String reason) {
		this.jsonReason = "{\"text\":\"" + reason + "\"}";
	}

	public PacketLoginOutDisconnect(ByteBufWrapper bytes) {
		this.jsonReason = bytes.readString();
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
		return ConnectionState.STATUS;
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
		bytes.writeString(this.jsonReason);
		return bytes.getBytes();
	}
}
