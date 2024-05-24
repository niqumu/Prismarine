package app.prismarine.server.net.packet.configuration;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PacketConfigurationOutFeatures implements Packet {

	private List<String> features = new ArrayList<>();

	public PacketConfigurationOutFeatures(String... features) {
		this.features = List.of(features);
	}

	public PacketConfigurationOutFeatures(ByteBufWrapper bytes) {
		int count = bytes.readVarInt();

		for (int i = 0; i < count; i++) {
			this.features.add(bytes.readString());
		}
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
		return 0xc;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeVarInt(this.features.size());
		this.features.forEach(bytes::writeString);
		return bytes.getBytes();
	}
}
