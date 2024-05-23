package app.prismarine.server.net.packet.configuration;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @see app.prismarine.server.player.PlayerConfiguration
 */
@Data
@AllArgsConstructor
public class PacketConfigurationInConfig implements Packet {

	/**
	 * The locale of the client, i.e. "en_US"
	 */
	private final String locale;

	/**
	 * Client-side render distance, in chunks
	 */
	private final int viewDistance;

	/**
	 * 0: enabled (all chat is displayed)
	 * <br>
	 * 1: commands only (only feedback from running commands is displayed)
	 * <br>
	 * 2: hidden entirely (no chat messages are displayed)
	 */
	private final int chatMode;

	/**
	 * Whether chat colors are enabled in the client's multiplayer settings
	 */
	private final boolean chatColors;

	/**
	 * Bit mask of the client's displayed skin parts
	 */
	private final byte skinParts;

	/**
	 * 0: left
	 * 1: right
	 */
	private final int mainHand;

	/**
	 * Enables filtering of text on signs and in book titles
	 */
	private final boolean textFiltering;

	/**
	 * Whether this player may appear in the server list player preview
	 */
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
