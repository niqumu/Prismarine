package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.primitive.StringTag;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Arrays;

@Data
public class PacketPlayOutSystemMessage implements Packet {

	private final String message;
	private final boolean actionBar;

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
		return ConnectionState.PLAY;
	}

	/**
	 * @return The internal ID of the packet
	 */
	@Override
	public int getID() {
		return 0x6c;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override @SneakyThrows
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();

		CompoundTag root = new CompoundTag("");
		root.put("text", new StringTag(this.message));
		bytes.writeBytes(PrismarineServer.NBT.toNetworkByteArray(root));

		bytes.writeBoolean(this.actionBar);
		return bytes.getBytes();
	}
}
