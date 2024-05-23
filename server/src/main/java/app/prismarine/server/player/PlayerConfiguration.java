package app.prismarine.server.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerConfiguration {

	/**
	 * Whether this configuration is using real values sent by the client. If false, default values are being used.
	 * If true, the client has sent a {@link app.prismarine.server.net.packet.configuration.PacketConfigurationInConfig}
	 * packet, updating these values
	 */
	private boolean updated = false;

	/**
	 * The locale of the client, i.e. "en_US"
	 */
	private String locale = "en_US";

	/**
	 * Client-side render distance, in chunks
	 */
	private int viewDistance = 16;

	/**
	 * 0: enabled (all chat is displayed)
	 * <br>
	 * 1: commands only (only feedback from running commands is displayed)
	 * <br>
	 * 2: hidden entirely (no chat messages are displayed)
	 */
	private int chatMode = 0;

	/**
	 * Whether chat colors are enabled in the client's multiplayer settings
	 */
	private boolean chatColors = true;

	/**
	 * Bit mask of the client's displayed skin parts
	 */
	private byte skinParts = 0b01111111;

	/**
	 * 0: left
	 * 1: right
	 */
	private int mainHand = 1;

	/**
	 * Enables filtering of text on signs and in book titles
	 */
	private boolean textFiltering = false;

	/**
	 * Whether this player may appear in the server list player preview
	 */
	private boolean allowServerListings = false;
}
