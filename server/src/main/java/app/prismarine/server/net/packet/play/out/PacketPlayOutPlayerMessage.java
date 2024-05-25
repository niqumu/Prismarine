package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class PacketPlayOutPlayerMessage implements Packet {

	private final String message;
	private final Player sender;

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
		return 0x39;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();

		// Header
		bytes.writeUUID(this.sender.getUniqueId());
		bytes.writeVarInt(0);
		bytes.writeBoolean(false);

		// Body
		bytes.writeString(this.message);
		bytes.writeLong(System.currentTimeMillis());
		bytes.writeLong(0);

		// Previous message
		bytes.writeVarInt(0);

		// Other
		bytes.writeBoolean(false);
		bytes.writeVarInt(0);

		// Chat formatting
		bytes.writeVarInt(0);
		bytes.writeString(sender.getDisplayName());
		bytes.writeBoolean(false);

		return bytes.getBytes();
	}
}
