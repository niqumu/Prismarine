package app.prismarine.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

public class ByteBufWrapper {

	private static final int SEGMENT_BITS = 0x7F;
	private static final int CONTINUE_BIT = 0x80;

	@Getter
	private final ByteBuf byteBuf;

	public ByteBufWrapper() {
		this(Unpooled.buffer());
	}

	public ByteBufWrapper(@NonNull ByteBuf byteBuf) {
		this.byteBuf = byteBuf;
	}

	public byte[] getBytes() {
		final int length = this.byteBuf.writerIndex();
		final int reader = this.byteBuf.readerIndex();
		this.byteBuf.readerIndex(0);
		final byte[] bytes = new byte[length];
		this.byteBuf.readBytes(bytes);
		this.byteBuf.readerIndex(reader);
		return bytes;
	}

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

	public String readString() {
		int length = this.readVarInt();
		byte[] stringBytes = new byte[length];

		for (int i = 0; i < length; i++) {
			stringBytes[i] = this.byteBuf.readByte();
		}

		return new String(stringBytes);
	}

	public void writeString(@NonNull String string) {
		byte[] stringBytes = string.getBytes();
		this.writeVarInt(stringBytes.length);
		this.getByteBuf().writeBytes(stringBytes);
	}

	public UUID readUUID() {
		return new UUID(this.readLong(), this.readLong());
	}

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

	public byte readByte() {
		return this.byteBuf.readByte();
	}

	public void writeByte(int data) {
		this.byteBuf.writeByte(data);
	}

	public void writeBytes(byte data[]) {
		this.byteBuf.writeBytes(data);
	}

	public boolean readBoolean() {
		return this.byteBuf.readBoolean();
	}

	public void writeBoolean(boolean data) {
		this.byteBuf.writeBoolean(data);
	}
}
