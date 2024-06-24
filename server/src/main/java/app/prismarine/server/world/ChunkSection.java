package app.prismarine.server.world;

import app.prismarine.server.util.ByteBufWrapper;
import dev.dewy.nbt.tags.collection.CompoundTag;
import org.bukkit.Chunk;

public class ChunkSection {

	/**
	 * The amount of non-air blocks in this chunk section
	 */
	private int count;

	/**
	 * The {@link Chunk} that this chunk section belongs to
	 */
	private final Chunk parent;

	/**
	 * The Y-layer of this chunk section within the parent chunk
	 */
	private final int layer;

	/**
	 * Constructs a new blank chunk section
	 * @param parent The parent chunk
	 * @param layer The layer of this chunk
	 */
	public ChunkSection(Chunk parent, int layer) {
		this.parent = parent;
		this.layer = layer;
	}

	/**
	 * Constructs a new chunk section from world-save NBT
	 * @param parent The parent chunk
	 * @param nbt The NBT representing this chunk
	 */
	public ChunkSection(Chunk parent, CompoundTag nbt) {
		this.parent = parent;
		this.layer = nbt.getByte("Y").getValue();
		this.parseBlockStates(nbt.getCompound("block_states"));
		this.parseBiomes(nbt.getCompound("biomes"));
	}

	/**
	 * Load block states from NBT
	 * @param blockStates NBT representation of the block states to load
	 */
	private void parseBlockStates(CompoundTag blockStates) {

	}

	/**
	 * Load biomes from NBT
	 * @param biomes NBT representation of the biomes to load
	 */
	private void parseBiomes(CompoundTag biomes) {

	}

	public byte[] getSerializedData() {
		ByteBufWrapper data = new ByteBufWrapper();
//		data.writeShort(this.count);
		data.writeShort(10);
		data.writeBytes(this.getBlockStates());
		data.writeBytes(this.getBiomes());
		return data.getBytes();
	}

	private byte[] getBlockStates() {

		int block = 0;

		if (Math.abs(parent.getX()) <= 2 && Math.abs(parent.getZ()) <= 2) {
			if (this.layer <= 3) {
				block = (parent.getX() + parent.getZ() + layer) % 2 == 0 ? 1 : 2;
			}
		}

		ByteBufWrapper data = new ByteBufWrapper();
		data.writeByte(0);
		data.writeVarInt(block);
		data.writeByte(0);
		return data.getBytes();
	}

	private byte[] getBiomes() {
		ByteBufWrapper data = new ByteBufWrapper();
		data.writeByte(0);
		data.writeVarInt(1);
		data.writeByte(0);
		return data.getBytes();
	}
}
