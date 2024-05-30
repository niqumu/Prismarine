package app.prismarine.server.net.packet.configuration;

import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PacketConfigurationInPacks implements Packet {

	private List<Pack> packs = new ArrayList<>();

	public PacketConfigurationInPacks(String namespace, String id, String version) {
		this.packs.add(new Pack(namespace, id, version));
	}

	public PacketConfigurationInPacks(ByteBufWrapper bytes) {
		int count = bytes.readVarInt();

		for (int i = 0; i < count; i++) {
			this.packs.add(new Pack(bytes));
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
		return ConnectionState.CONFIGURATION;
	}

	/**
	 * @return The internal ID of the packet
	 */
	@Override
	public int getID() {
		return 0x7;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeVarInt(this.packs.size());
		this.packs.forEach(pack -> pack.writeTo(bytes));
		return bytes.getBytes();
	}

	@Data
	@AllArgsConstructor
	private static class Pack {
		private String namespace;
		private String id;
		private String version;

		public Pack(ByteBufWrapper bytes) {
			this.readFrom(bytes);
		}

		public void readFrom(ByteBufWrapper bytes) {
			this.namespace = bytes.readString();
			this.id = bytes.readString();
			this.version = bytes.readString();
		}

		public void writeTo(ByteBufWrapper bytes) {
			bytes.writeString(this.namespace);
			bytes.writeString(this.id);
			bytes.writeString(this.version);
		}
	}
}
