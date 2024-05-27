package app.prismarine.server.world;

import app.prismarine.server.net.ByteBufWrapper;
import org.bukkit.Chunk;

public class ChunkSection {

	/**
	 * The amount of non-air blocks in this chunk section
	 */
	private int count;

	private final Chunk parent;

	private final int layer;

	public ChunkSection(Chunk parent, int layer) {
		this.parent = parent;
		this.layer = layer;
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
