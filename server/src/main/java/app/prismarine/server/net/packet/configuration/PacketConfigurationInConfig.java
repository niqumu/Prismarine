package app.prismarine.server.net.packet.configuration;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PacketConfigurationInConfig implements Packet {

	private final String locale;
	private final int viewDistance;
	private final int chatMode;
	private final boolean chatColors;
	private final byte skinParts;
	private final int mainHand;
	private final boolean textFiltering;
	private final boolean allowServerListings;

	public PacketConfigurationInConfig(ByteBufWrapper bytes) {
		this.locale = bytes.readString();
		this.viewDistance = bytes.readByte();
		this.chatMode = bytes.readVarInt();
		this.chatColors = bytes.readBoolean();
		this.skinParts = bytes.readByte();
		this.mainHand = bytes.readVarInt();
		this.textFiltering = bytes.readBoolean();
		this.allowServerListings = bytes.readBoolean();
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
		return 0x0;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeString(this.locale);
		bytes.writeByte(this.viewDistance);
		bytes.writeVarInt(this.chatMode);
		bytes.writeBoolean(this.chatColors);
		bytes.writeByte(this.skinParts);
		bytes.writeVarInt(this.mainHand);
		bytes.writeBoolean(this.textFiltering);
		bytes.writeBoolean(this.allowServerListings);
		return bytes.getBytes();
	}
}
