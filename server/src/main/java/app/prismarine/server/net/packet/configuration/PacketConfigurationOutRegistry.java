package app.prismarine.server.net.packet.configuration;

import app.prismarine.server.util.ByteBufWrapper;
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
				this.registryEntries.add(new RegistryEntry("badlands"));
				this.registryEntries.add(new RegistryEntry("bamboo_jungle"));
				this.registryEntries.add(new RegistryEntry("basalt_deltas"));
				this.registryEntries.add(new RegistryEntry("beach"));
				this.registryEntries.add(new RegistryEntry("birch_forest"));
				this.registryEntries.add(new RegistryEntry("cherry_grove"));
				this.registryEntries.add(new RegistryEntry("cold_ocean"));
				this.registryEntries.add(new RegistryEntry("crimson_forest"));
				this.registryEntries.add(new RegistryEntry("dark_forest"));
				this.registryEntries.add(new RegistryEntry("deep_cold_ocean"));
				this.registryEntries.add(new RegistryEntry("deep_dark"));
				this.registryEntries.add(new RegistryEntry("deep_frozen_ocean"));
				this.registryEntries.add(new RegistryEntry("deep_lukewarm_ocean"));
				this.registryEntries.add(new RegistryEntry("deep_ocean"));
				this.registryEntries.add(new RegistryEntry("desert"));
				this.registryEntries.add(new RegistryEntry("dripstone_caves"));
				this.registryEntries.add(new RegistryEntry("end_barrens"));
				this.registryEntries.add(new RegistryEntry("end_highlands"));
				this.registryEntries.add(new RegistryEntry("end_midlands"));
				this.registryEntries.add(new RegistryEntry("eroded_badlands"));
				this.registryEntries.add(new RegistryEntry("flower_forest"));
				this.registryEntries.add(new RegistryEntry("forest"));
				this.registryEntries.add(new RegistryEntry("frozen_ocean"));
				this.registryEntries.add(new RegistryEntry("frozen_peaks"));
				this.registryEntries.add(new RegistryEntry("frozen_river"));
				this.registryEntries.add(new RegistryEntry("grove"));
				this.registryEntries.add(new RegistryEntry("ice_spikes"));
				this.registryEntries.add(new RegistryEntry("jagged_peaks"));
				this.registryEntries.add(new RegistryEntry("jungle"));
				this.registryEntries.add(new RegistryEntry("lukewarm_ocean"));
				this.registryEntries.add(new RegistryEntry("lush_caves"));
				this.registryEntries.add(new RegistryEntry("mangrove_swamp"));
				this.registryEntries.add(new RegistryEntry("meadow"));
				this.registryEntries.add(new RegistryEntry("mushroom_fields"));
				this.registryEntries.add(new RegistryEntry("nether_wastes"));
				this.registryEntries.add(new RegistryEntry("ocean"));
				this.registryEntries.add(new RegistryEntry("old_growth_birch_forest"));
				this.registryEntries.add(new RegistryEntry("old_growth_pine_taiga"));
				this.registryEntries.add(new RegistryEntry("old_growth_spruce_taiga"));
				this.registryEntries.add(new RegistryEntry("plains"));
				this.registryEntries.add(new RegistryEntry("river"));
				this.registryEntries.add(new RegistryEntry("savanna"));
				this.registryEntries.add(new RegistryEntry("savanna_plateau"));
				this.registryEntries.add(new RegistryEntry("small_end_islands"));
				this.registryEntries.add(new RegistryEntry("snowy_beach"));
				this.registryEntries.add(new RegistryEntry("snowy_plains"));
				this.registryEntries.add(new RegistryEntry("snowy_slopes"));
				this.registryEntries.add(new RegistryEntry("snowy_taiga"));
				this.registryEntries.add(new RegistryEntry("soul_sand_valley"));
				this.registryEntries.add(new RegistryEntry("sparse_jungle"));
				this.registryEntries.add(new RegistryEntry("stony_peaks"));
				this.registryEntries.add(new RegistryEntry("stony_shore"));
				this.registryEntries.add(new RegistryEntry("sunflower_plains"));
				this.registryEntries.add(new RegistryEntry("swamp"));
				this.registryEntries.add(new RegistryEntry("taiga"));
				this.registryEntries.add(new RegistryEntry("the_end"));
				this.registryEntries.add(new RegistryEntry("the_void"));
				this.registryEntries.add(new RegistryEntry("warm_ocean"));
				this.registryEntries.add(new RegistryEntry("warped_forest"));
				this.registryEntries.add(new RegistryEntry("windswept_forest"));
				this.registryEntries.add(new RegistryEntry("windswept_gravelly_hills"));
				this.registryEntries.add(new RegistryEntry("windswept_hills"));
				this.registryEntries.add(new RegistryEntry("windswept_savanna"));
				this.registryEntries.add(new RegistryEntry("wooded_badlands"));
			}
			case CHAT_TYPE -> {
				this.registryEntries.add(new RegistryEntry("chat"));
				this.registryEntries.add(new RegistryEntry("emote_command"));
				this.registryEntries.add(new RegistryEntry("msg_command_incoming"));
				this.registryEntries.add(new RegistryEntry("msg_command_outgoing"));
				this.registryEntries.add(new RegistryEntry("say_command"));
				this.registryEntries.add(new RegistryEntry("team_msg_command_incoming"));
				this.registryEntries.add(new RegistryEntry("team_msg_command_outgoing"));
			}
			case TRIM_PATTERN -> {
				this.registryEntries.add(new RegistryEntry("coast"));
				this.registryEntries.add(new RegistryEntry("dune"));
				this.registryEntries.add(new RegistryEntry("eye"));
				this.registryEntries.add(new RegistryEntry("host"));
				this.registryEntries.add(new RegistryEntry("raiser"));
				this.registryEntries.add(new RegistryEntry("rib"));
				this.registryEntries.add(new RegistryEntry("sentry"));
				this.registryEntries.add(new RegistryEntry("shaper"));
				this.registryEntries.add(new RegistryEntry("silence"));
				this.registryEntries.add(new RegistryEntry("snout"));
				this.registryEntries.add(new RegistryEntry("spire"));
				this.registryEntries.add(new RegistryEntry("tide"));
				this.registryEntries.add(new RegistryEntry("vex"));
				this.registryEntries.add(new RegistryEntry("ward"));
				this.registryEntries.add(new RegistryEntry("wayfinder"));
				this.registryEntries.add(new RegistryEntry("wild"));
			}
			case TRIM_MATERIAL -> {
				this.registryEntries.add(new RegistryEntry("amethyst"));
				this.registryEntries.add(new RegistryEntry("copper"));
				this.registryEntries.add(new RegistryEntry("diamond"));
				this.registryEntries.add(new RegistryEntry("emerald"));
				this.registryEntries.add(new RegistryEntry("gold"));
				this.registryEntries.add(new RegistryEntry("iron"));
				this.registryEntries.add(new RegistryEntry("lapis"));
				this.registryEntries.add(new RegistryEntry("netherite"));
				this.registryEntries.add(new RegistryEntry("quartz"));
				this.registryEntries.add(new RegistryEntry("redstone"));
			}
			case WOLF_VARIANT -> {
				this.registryEntries.add(new RegistryEntry("ashen"));
				this.registryEntries.add(new RegistryEntry("black"));
				this.registryEntries.add(new RegistryEntry("chestnut"));
				this.registryEntries.add(new RegistryEntry("pale"));
				this.registryEntries.add(new RegistryEntry("rusty"));
				this.registryEntries.add(new RegistryEntry("snowy"));
				this.registryEntries.add(new RegistryEntry("spotted"));
				this.registryEntries.add(new RegistryEntry("striped"));
				this.registryEntries.add(new RegistryEntry("woods"));
			}
			case PAINTING_VARIANT -> {
				this.registryEntries.add(new RegistryEntry("alban"));
				this.registryEntries.add(new RegistryEntry("aztec"));
				this.registryEntries.add(new RegistryEntry("aztec2"));
				this.registryEntries.add(new RegistryEntry("backyard"));
				this.registryEntries.add(new RegistryEntry("baroque"));
				this.registryEntries.add(new RegistryEntry("bomb"));
				this.registryEntries.add(new RegistryEntry("bouquet"));
				this.registryEntries.add(new RegistryEntry("burning_skull"));
				this.registryEntries.add(new RegistryEntry("bust"));
				this.registryEntries.add(new RegistryEntry("cavebird"));
				this.registryEntries.add(new RegistryEntry("changing"));
				this.registryEntries.add(new RegistryEntry("cotan"));
				this.registryEntries.add(new RegistryEntry("courbet"));
				this.registryEntries.add(new RegistryEntry("creebet"));
				this.registryEntries.add(new RegistryEntry("donkey_kong"));
				this.registryEntries.add(new RegistryEntry("earth"));
				this.registryEntries.add(new RegistryEntry("endboss"));
				this.registryEntries.add(new RegistryEntry("fern"));
				this.registryEntries.add(new RegistryEntry("fighters"));
				this.registryEntries.add(new RegistryEntry("finding"));
				this.registryEntries.add(new RegistryEntry("fire"));
				this.registryEntries.add(new RegistryEntry("graham"));
				this.registryEntries.add(new RegistryEntry("humble"));
				this.registryEntries.add(new RegistryEntry("kebab"));
				this.registryEntries.add(new RegistryEntry("lowmist"));
				this.registryEntries.add(new RegistryEntry("match"));
				this.registryEntries.add(new RegistryEntry("meditative"));
				this.registryEntries.add(new RegistryEntry("orb"));
				this.registryEntries.add(new RegistryEntry("owlemons"));
				this.registryEntries.add(new RegistryEntry("passage"));
				this.registryEntries.add(new RegistryEntry("pigscene"));
				this.registryEntries.add(new RegistryEntry("plant"));
				this.registryEntries.add(new RegistryEntry("pointer"));
				this.registryEntries.add(new RegistryEntry("pond"));
				this.registryEntries.add(new RegistryEntry("pool"));
				this.registryEntries.add(new RegistryEntry("prairie_ride"));
				this.registryEntries.add(new RegistryEntry("sea"));
				this.registryEntries.add(new RegistryEntry("skeleton"));
				this.registryEntries.add(new RegistryEntry("skull_and_roses"));
				this.registryEntries.add(new RegistryEntry("stage"));
				this.registryEntries.add(new RegistryEntry("sunflowers"));
				this.registryEntries.add(new RegistryEntry("sunset"));
				this.registryEntries.add(new RegistryEntry("tides"));
				this.registryEntries.add(new RegistryEntry("unpacked"));
				this.registryEntries.add(new RegistryEntry("void"));
				this.registryEntries.add(new RegistryEntry("wanderer"));
				this.registryEntries.add(new RegistryEntry("wasteland"));
				this.registryEntries.add(new RegistryEntry("water"));
				this.registryEntries.add(new RegistryEntry("wind"));
				this.registryEntries.add(new RegistryEntry("wither"));
			}
			case DIMENSION_TYPE -> {
				this.registryEntries.add(new RegistryEntry("overworld"));
				this.registryEntries.add(new RegistryEntry("overworld_caves"));
				this.registryEntries.add(new RegistryEntry("the_end"));
				this.registryEntries.add(new RegistryEntry("the_nether"));
			}
			case DAMAGE_TYPE -> {
				this.registryEntries.add(new RegistryEntry("arrow"));
				this.registryEntries.add(new RegistryEntry("bad_respawn_point"));
				this.registryEntries.add(new RegistryEntry("cactus"));
				this.registryEntries.add(new RegistryEntry("campfire"));
				this.registryEntries.add(new RegistryEntry("cramming"));
				this.registryEntries.add(new RegistryEntry("dragon_breath"));
				this.registryEntries.add(new RegistryEntry("drown"));
				this.registryEntries.add(new RegistryEntry("dry_out"));
				this.registryEntries.add(new RegistryEntry("explosion"));
				this.registryEntries.add(new RegistryEntry("fall"));
				this.registryEntries.add(new RegistryEntry("falling_anvil"));
				this.registryEntries.add(new RegistryEntry("falling_block"));
				this.registryEntries.add(new RegistryEntry("falling_stalactite"));
				this.registryEntries.add(new RegistryEntry("fireball"));
				this.registryEntries.add(new RegistryEntry("fireworks"));
				this.registryEntries.add(new RegistryEntry("fly_into_wall"));
				this.registryEntries.add(new RegistryEntry("freeze"));
				this.registryEntries.add(new RegistryEntry("generic"));
				this.registryEntries.add(new RegistryEntry("generic_kill"));
				this.registryEntries.add(new RegistryEntry("hot_floor"));
				this.registryEntries.add(new RegistryEntry("in_fire"));
				this.registryEntries.add(new RegistryEntry("in_wall"));
				this.registryEntries.add(new RegistryEntry("indirect_magic"));
				this.registryEntries.add(new RegistryEntry("lava"));
				this.registryEntries.add(new RegistryEntry("lightning_bolt"));
				this.registryEntries.add(new RegistryEntry("magic"));
				this.registryEntries.add(new RegistryEntry("mob_attack"));
				this.registryEntries.add(new RegistryEntry("mob_attack_no_aggro"));
				this.registryEntries.add(new RegistryEntry("mob_projectile"));
				this.registryEntries.add(new RegistryEntry("on_fire"));
				this.registryEntries.add(new RegistryEntry("out_of_world"));
				this.registryEntries.add(new RegistryEntry("outside_border"));
				this.registryEntries.add(new RegistryEntry("player_attack"));
				this.registryEntries.add(new RegistryEntry("player_explosion"));
				this.registryEntries.add(new RegistryEntry("sonic_boom"));
				this.registryEntries.add(new RegistryEntry("spit"));
				this.registryEntries.add(new RegistryEntry("stalagmite"));
				this.registryEntries.add(new RegistryEntry("starve"));
				this.registryEntries.add(new RegistryEntry("sting"));
				this.registryEntries.add(new RegistryEntry("sweet_berry_bush"));
				this.registryEntries.add(new RegistryEntry("thorns"));
				this.registryEntries.add(new RegistryEntry("trident"));
				this.registryEntries.add(new RegistryEntry("unattributed_fireball"));
				this.registryEntries.add(new RegistryEntry("wither"));
				this.registryEntries.add(new RegistryEntry("wither_skull"));
			}
			case BANNER_PATTERN -> {
				this.registryEntries.add(new RegistryEntry("base"));
				this.registryEntries.add(new RegistryEntry("border"));
				this.registryEntries.add(new RegistryEntry("bricks"));
				this.registryEntries.add(new RegistryEntry("circle"));
				this.registryEntries.add(new RegistryEntry("creeper"));
				this.registryEntries.add(new RegistryEntry("cross"));
				this.registryEntries.add(new RegistryEntry("curly_border"));
				this.registryEntries.add(new RegistryEntry("diagonal_left"));
				this.registryEntries.add(new RegistryEntry("diagonal_right"));
				this.registryEntries.add(new RegistryEntry("diagonal_up_left"));
				this.registryEntries.add(new RegistryEntry("diagonal_up_right"));
				this.registryEntries.add(new RegistryEntry("flow"));
				this.registryEntries.add(new RegistryEntry("flower"));
				this.registryEntries.add(new RegistryEntry("globe"));
				this.registryEntries.add(new RegistryEntry("gradient"));
				this.registryEntries.add(new RegistryEntry("gradient_up"));
				this.registryEntries.add(new RegistryEntry("guster"));
				this.registryEntries.add(new RegistryEntry("half_horizontal"));
				this.registryEntries.add(new RegistryEntry("half_horizontal_bottom"));
				this.registryEntries.add(new RegistryEntry("half_vertical"));
				this.registryEntries.add(new RegistryEntry("half_vertical_right"));
				this.registryEntries.add(new RegistryEntry("mojang"));
				this.registryEntries.add(new RegistryEntry("piglin"));
				this.registryEntries.add(new RegistryEntry("rhombus"));
				this.registryEntries.add(new RegistryEntry("skull"));
				this.registryEntries.add(new RegistryEntry("small_stripes"));
				this.registryEntries.add(new RegistryEntry("square_bottom_left"));
				this.registryEntries.add(new RegistryEntry("square_bottom_right"));
				this.registryEntries.add(new RegistryEntry("square_top_left"));
				this.registryEntries.add(new RegistryEntry("square_top_right"));
				this.registryEntries.add(new RegistryEntry("straight_cross"));
				this.registryEntries.add(new RegistryEntry("stripe_bottom"));
				this.registryEntries.add(new RegistryEntry("stripe_center"));
				this.registryEntries.add(new RegistryEntry("stripe_downleft"));
				this.registryEntries.add(new RegistryEntry("stripe_downright"));
				this.registryEntries.add(new RegistryEntry("stripe_left"));
				this.registryEntries.add(new RegistryEntry("stripe_middle"));
				this.registryEntries.add(new RegistryEntry("stripe_right"));
				this.registryEntries.add(new RegistryEntry("stripe_top"));
				this.registryEntries.add(new RegistryEntry("triangle_bottom"));
				this.registryEntries.add(new RegistryEntry("triangle_top"));
				this.registryEntries.add(new RegistryEntry("triangles_bottom"));
				this.registryEntries.add(new RegistryEntry("triangles_top"));
			}
			case ENCHANTMENT -> {
				this.registryEntries.add(new RegistryEntry("aqua_affinity"));
			}
			case JUKEBOX_SONG -> {
				this.registryEntries.add(new RegistryEntry("11"));
			}
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
		PAINTING_VARIANT("painting_variant"),
		DIMENSION_TYPE("dimension_type"),
		DAMAGE_TYPE("damage_type"),
		BANNER_PATTERN("banner_pattern"),
		ENCHANTMENT("enchantment"),
		JUKEBOX_SONG("jukebox_song");

		private final String identifier;
	}

	private record RegistryEntry(String identifier, boolean hasData) {
		
		public RegistryEntry(String identifier) {
			this(identifier, false);
		}
		
		public void writeTo(ByteBufWrapper wrapper) {
			wrapper.writeIdentifier("minecraft", identifier);
			wrapper.writeBoolean(hasData);
		}
	}
}
