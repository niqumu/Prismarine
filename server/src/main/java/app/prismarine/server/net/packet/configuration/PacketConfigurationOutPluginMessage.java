package app.prismarine.server.net.packet.configuration;

import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PacketConfigurationOutPluginMessage implements Packet {

	private final String channel;
	private final byte[] data;

	public PacketConfigurationOutPluginMessage(String channel, String stringData) {
		this.channel = channel;

		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeString(stringData);
		this.data = bytes.getBytes();
	}

	public PacketConfigurationOutPluginMessage(ByteBufWrapper bytes) {
		this.channel = bytes.readString();
		this.data = bytes.readRemainingBytes();
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
		return ConnectionState.CONFIGURATION;
	}

	/**
	 * @return The internal ID of the packet
	 */
	@Override
	public int getID() {
		return 0x1;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeString(this.channel);
		bytes.writeBytes(this.data);
		return bytes.getBytes();
	}
}
