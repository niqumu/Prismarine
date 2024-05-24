package app.prismarine.server.net.packet.configuration;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public class PacketConfigurationOutRegistry implements Packet {

	private final RegistryType registryType;

	private final List<RegistryEntry> registryEntries = new ArrayList<>();

	public PacketConfigurationOutRegistry(RegistryType registryType) {
		this.registryType = registryType;

		// todo move this monstrosity to a json
		switch (this.registryType) {
			case BIOME -> {
				this.registryEntries.add(new RegistryEntry("badlands", false));
				this.registryEntries.add(new RegistryEntry("bamboo_jungle", false));
				this.registryEntries.add(new RegistryEntry("basalt_deltas", false));
				this.registryEntries.add(new RegistryEntry("beach", false));
				this.registryEntries.add(new RegistryEntry("birch_forest", false));
				this.registryEntries.add(new RegistryEntry("cherry_grove", false));
				this.registryEntries.add(new RegistryEntry("cold_ocean", false));
				this.registryEntries.add(new RegistryEntry("crimson_forest", false));
				this.registryEntries.add(new RegistryEntry("dark_forest", false));
				this.registryEntries.add(new RegistryEntry("deep_cold_ocean", false));
				this.registryEntries.add(new RegistryEntry("deep_dark", false));
				this.registryEntries.add(new RegistryEntry("deep_frozen_ocean", false));
				this.registryEntries.add(new RegistryEntry("deep_lukewarm_ocean", false));
				this.registryEntries.add(new RegistryEntry("deep_ocean", false));
				this.registryEntries.add(new RegistryEntry("desert", false));
				this.registryEntries.add(new RegistryEntry("dripstone_caves", false));
				this.registryEntries.add(new RegistryEntry("end_barrens", false));
				this.registryEntries.add(new RegistryEntry("end_highlands", false));
				this.registryEntries.add(new RegistryEntry("end_midlands", false));
				this.registryEntries.add(new RegistryEntry("eroded_badlands", false));
				this.registryEntries.add(new RegistryEntry("flower_forest", false));
				this.registryEntries.add(new RegistryEntry("forest", false));
				this.registryEntries.add(new RegistryEntry("frozen_ocean", false));
				this.registryEntries.add(new RegistryEntry("frozen_peaks", false));
				this.registryEntries.add(new RegistryEntry("frozen_river", false));
				this.registryEntries.add(new RegistryEntry("grove", false));
				this.registryEntries.add(new RegistryEntry("ice_spikes", false));
				this.registryEntries.add(new RegistryEntry("jagged_peaks", false));
				this.registryEntries.add(new RegistryEntry("jungle", false));
				this.registryEntries.add(new RegistryEntry("lukewarm_ocean", false));
				this.registryEntries.add(new RegistryEntry("lush_caves", false));
				this.registryEntries.add(new RegistryEntry("mangrove_swamp", false));
				this.registryEntries.add(new RegistryEntry("meadow", false));
				this.registryEntries.add(new RegistryEntry("mushroom_fields", false));
				this.registryEntries.add(new RegistryEntry("nether_wastes", false));
				this.registryEntries.add(new RegistryEntry("ocean", false));
				this.registryEntries.add(new RegistryEntry("old_growth_birch_forest", false));
				this.registryEntries.add(new RegistryEntry("old_growth_pine_taiga", false));
				this.registryEntries.add(new RegistryEntry("old_growth_spruce_taiga", false));
				this.registryEntries.add(new RegistryEntry("plains", false));
				this.registryEntries.add(new RegistryEntry("river", false));
				this.registryEntries.add(new RegistryEntry("savanna", false));
				this.registryEntries.add(new RegistryEntry("savanna_plateau", false));
				this.registryEntries.add(new RegistryEntry("small_end_islands", false));
				this.registryEntries.add(new RegistryEntry("snowy_beach", false));
				this.registryEntries.add(new RegistryEntry("snowy_plains", false));
				this.registryEntries.add(new RegistryEntry("snowy_slopes", false));
				this.registryEntries.add(new RegistryEntry("snowy_taiga", false));
				this.registryEntries.add(new RegistryEntry("soul_sand_valley", false));
				this.registryEntries.add(new RegistryEntry("sparse_jungle", false));
				this.registryEntries.add(new RegistryEntry("stony_peaks", false));
				this.registryEntries.add(new RegistryEntry("stony_shore", false));
				this.registryEntries.add(new RegistryEntry("sunflower_plains", false));
				this.registryEntries.add(new RegistryEntry("swamp", false));
				this.registryEntries.add(new RegistryEntry("taiga", false));
				this.registryEntries.add(new RegistryEntry("the_end", false));
				this.registryEntries.add(new RegistryEntry("the_void", false));
				this.registryEntries.add(new RegistryEntry("warm_ocean", false));
				this.registryEntries.add(new RegistryEntry("warped_forest", false));
				this.registryEntries.add(new RegistryEntry("windswept_forest", false));
				this.registryEntries.add(new RegistryEntry("windswept_gravelly_hills", false));
				this.registryEntries.add(new RegistryEntry("windswept_hills", false));
				this.registryEntries.add(new RegistryEntry("windswept_savanna", false));
				this.registryEntries.add(new RegistryEntry("wooded_badlands", false));
			}
			case CHAT_TYPE -> {
				this.registryEntries.add(new RegistryEntry("chat", false));
				this.registryEntries.add(new RegistryEntry("emote_command", false));
				this.registryEntries.add(new RegistryEntry("msg_command_incoming", false));
				this.registryEntries.add(new RegistryEntry("msg_command_outgoing", false));
				this.registryEntries.add(new RegistryEntry("say_command", false));
				this.registryEntries.add(new RegistryEntry("team_msg_command_incoming", false));
				this.registryEntries.add(new RegistryEntry("team_msg_command_outgoing", false));
			}
			case TRIM_PATTERN -> {
				this.registryEntries.add(new RegistryEntry("coast", false));
				this.registryEntries.add(new RegistryEntry("dune", false));
				this.registryEntries.add(new RegistryEntry("eye", false));
				this.registryEntries.add(new RegistryEntry("host", false));
				this.registryEntries.add(new RegistryEntry("raiser", false));
				this.registryEntries.add(new RegistryEntry("rib", false));
				this.registryEntries.add(new RegistryEntry("sentry", false));
				this.registryEntries.add(new RegistryEntry("shaper", false));
				this.registryEntries.add(new RegistryEntry("silence", false));
				this.registryEntries.add(new RegistryEntry("snout", false));
				this.registryEntries.add(new RegistryEntry("spire", false));
				this.registryEntries.add(new RegistryEntry("tide", false));
				this.registryEntries.add(new RegistryEntry("vex", false));
				this.registryEntries.add(new RegistryEntry("ward", false));
				this.registryEntries.add(new RegistryEntry("wayfinder", false));
				this.registryEntries.add(new RegistryEntry("wild", false));
			}
			case TRIM_MATERIAL -> {
				this.registryEntries.add(new RegistryEntry("amethyst", false));
				this.registryEntries.add(new RegistryEntry("copper", false));
				this.registryEntries.add(new RegistryEntry("diamond", false));
				this.registryEntries.add(new RegistryEntry("emerald", false));
				this.registryEntries.add(new RegistryEntry("gold", false));
				this.registryEntries.add(new RegistryEntry("iron", false));
				this.registryEntries.add(new RegistryEntry("lapis", false));
				this.registryEntries.add(new RegistryEntry("netherite", false));
				this.registryEntries.add(new RegistryEntry("quartz", false));
				this.registryEntries.add(new RegistryEntry("redstone", false));
			}
			case WOLF_VARIANT -> {
				this.registryEntries.add(new RegistryEntry("ashen", false));
				this.registryEntries.add(new RegistryEntry("black", false));
				this.registryEntries.add(new RegistryEntry("chestnut", false));
				this.registryEntries.add(new RegistryEntry("pale", false));
				this.registryEntries.add(new RegistryEntry("rusty", false));
				this.registryEntries.add(new RegistryEntry("snowy", false));
				this.registryEntries.add(new RegistryEntry("spotted", false));
				this.registryEntries.add(new RegistryEntry("striped", false));
				this.registryEntries.add(new RegistryEntry("woods", false));
			}
			case DIMENSION_TYPE -> {
				this.registryEntries.add(new RegistryEntry("overworld", false));
				this.registryEntries.add(new RegistryEntry("overworld_caves", false));
				this.registryEntries.add(new RegistryEntry("the_end", false));
				this.registryEntries.add(new RegistryEntry("the_nether", false));
			}

			// TODO damage type, banner pattern (log from vanilla)
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
