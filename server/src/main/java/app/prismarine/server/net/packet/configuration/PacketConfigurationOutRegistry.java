package app.prismarine.server.net.packet.configuration;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PacketConfigurationOutRegistry implements Packet {

	private final RegistryType registryType;

	private final List<RegistryEntry> registryEntries = new ArrayList<>();

	public PacketConfigurationOutRegistry(RegistryType registryType) {
		this.registryType = registryType;

		// todo
		if (this.registryType.equals(RegistryType.DIMENSION_TYPE)) {
			this.registryEntries.add(new RegistryEntry("overworld", false));
			this.registryEntries.add(new RegistryEntry("overworld_caves", false));
			this.registryEntries.add(new RegistryEntry("the_end", false));
			this.registryEntries.add(new RegistryEntry("the_nether", false));
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
		return 0x7;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeIdentifier("minecraft", this.registryType.getIdentifier());

		// Write entries
		bytes.writeVarInt(this.registryEntries.size());
		this.registryEntries.forEach(entry -> entry.writeTo(bytes));

		return bytes.getBytes();
	}

	@Getter
	@AllArgsConstructor
	public enum RegistryType {
		BIOME("worldgen/biome"),
		CHAT_TYPE("chat_type"),
		TRIM_PATTERN("trim_pattern"),
		TRIM_MATERIAL("trim_material"),
		WOLF_VARIANT("wolf_variant"),
		DIMENSION_TYPE("dimension_type"),
		DAMAGE_TYPE("damage_type"),
		BANNER_PATTERN("banner_pattern");

		private final String identifier;
	}

	private record RegistryEntry(String identifier, boolean hasData) {
		public void writeTo(ByteBufWrapper wrapper) {
			wrapper.writeIdentifier("minecraft", identifier);
			wrapper.writeBoolean(hasData);
		}
	}
}
