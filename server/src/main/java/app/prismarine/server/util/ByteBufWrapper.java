package app.prismarine.server.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A wrapper for the netty {@link ByteBuf} class, providing convenient read/write methods for Minecraft data types
 * <p>
 * Portions of this class are heavily based off of code provided by wiki.vg.
 * @author chloe
 */
public class ByteBufWrapper {

	private static final int SEGMENT_BITS = 0x7F;
	private static final int CONTINUE_BIT = 0x80;

	/**
	 * The {@link ByteBuf} that this object is a wrapper for
	 */
	@Getter
	private final ByteBuf byteBuf;

	public ByteBufWrapper() {
		this(Unpooled.buffer());
	}

	public ByteBufWrapper(@NotNull ByteBuf byteBuf) {
		this.byteBuf = byteBuf;
	}

	/**
	 * Returns the contents of this wrapper as a byte array
	 * @return The complete contents of this wrapper as a byte[]
	 */
	public byte[] getBytes() {
		final int length = this.byteBuf.writerIndex();
		final int reader = this.byteBuf.readerIndex();
		this.byteBuf.readerIndex(0);
		final byte[] bytes = new byte[length];
		this.byteBuf.readBytes(bytes);
		this.byteBuf.readerIndex(reader);
		return bytes;
	}

	/**
	 * Reads a VarInt from the buffer at the current position
	 * @return An int representation of a VarInt
	 */
	public int readVarInt() {
		int value = 0;
		int position = 0;
		byte currentByte;

		while (true) {
			currentByte = this.readByte();
			value |= (currentByte & SEGMENT_BITS) << position;

			if ((currentByte & CONTINUE_BIT) == 0) break;

			position += 7;

			if (position >= 32) throw new RuntimeException("VarInt is too big");
		}

		return value;
	}

	/**
	 * Writes a VarInt to the buffer at the current position
	 * @param data An int to write to the buffer as a VarInt
	 */
	public void writeVarInt(int data) {
		while (true) {
			if ((data & ~SEGMENT_BITS) == 0) {
				this.writeByte(data);
				return;
			}

			this.writeByte((data & SEGMENT_BITS) | CONTINUE_BIT);

			data >>>= 7;
		}
	}

	/**
	 * Reads a Minecraft-style length-prefixed String from the buffer at the current position
	 * @return The string that was read from the buffer
	 */
	public String readString() {
		int length = this.readVarInt();
		byte[] stringBytes = new byte[length];

		for (int i = 0; i < length; i++) {
			stringBytes[i] = this.byteBuf.readByte();
		}

		return new String(stringBytes);
	}

	/**
	 * Writes a Minecraft-style length-prefixed String to the buffer at the current position
	 * @param string The String to write to buffer
	 */
	public void writeString(@NotNull String string) {
		byte[] stringBytes = string.getBytes();
		this.writeVarInt(stringBytes.length);
		this.getByteBuf().writeBytes(stringBytes);
	}

	/**
	 * Writes a Minecraft-style identifier to the buffer at the current position
	 * @param namespace The namespace of the identifier
	 * @param string The contents of the identifier
	 */
	public void writeIdentifier(@NotNull String namespace, @NotNull String string) {
		writeString(namespace + ":" + string);
	}

	/**
	 * Reads a UUID from the buffer at the current position
	 * @return The UUID that was read from the buffer
	 */
	public UUID readUUID() {
		return new UUID(this.readLong(), this.readLong());
	}

	/**
	 * Writes a UUID to the buffer at the current position
	 * @param data The UUID to write to the buffer
	 */
	public void writeUUID(UUID data) {
		this.writeLong(data.getMostSignificantBits());
		this.writeLong(data.getLeastSignificantBits());
	}

	public int readInt() {
		return this.byteBuf.readInt();
	}

	public void writeInt(int data) {
		this.byteBuf.writeInt(data);
	}

	public short readShort() {
		return this.byteBuf.readShort();
	}

	public void writeShort(int data) {
		this.byteBuf.writeShort(data);
	}

	public long readLong() {
		return this.byteBuf.readLong();
	}

	public void writeLong(long data) {
		this.byteBuf.writeLong(data);
	}

	public float readFloat() {
		return this.byteBuf.readFloat();
	}

	public void writeFloat(float data) {
		this.byteBuf.writeFloat(data);
	}

	public double readDouble() {
		return this.byteBuf.readDouble();
	}

	public void writeDouble(double data) {
		this.byteBuf.writeDouble(data);
	}

	public byte readByte() {
		return this.byteBuf.readByte();
	}

	public void writeByte(int data) {
		this.byteBuf.writeByte(data);
	}

	/**
	 * Reads {@code count} bytes from the buffer at the current position
	 * @param count The amount of bytes to read
	 * @return A byte array, {@code count} bytes long, of the data read from the buffer
	 */
	public byte[] readBytes(int count) {
		byte[] bytes = new byte[count];

		for (int i = 0; i < count; i++) {
			bytes[i] = this.readByte();
		}

		return bytes;
	}

	/**
	 * Reads the rest of the remaining unread bytes from the buffer
	 * @return A byte array of the remaining unread bytes in the buffer
	 */
	public byte[] readRemainingBytes() {
		final int length = this.byteBuf.writerIndex();
		final int reader = this.byteBuf.readerIndex();
		return this.readBytes(length - reader);
	}

	public void writeBytes(byte[] data) {
		this.byteBuf.writeBytes(data);
	}

	public boolean readBoolean() {
		return this.byteBuf.readBoolean();
	}

	public void writeBoolean(boolean data) {
		this.byteBuf.writeBoolean(data);
	}
}
