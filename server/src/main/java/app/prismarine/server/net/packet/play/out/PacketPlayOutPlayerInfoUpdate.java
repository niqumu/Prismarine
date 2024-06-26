package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.util.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
public class PacketPlayOutPlayerInfoUpdate implements Packet {

	private final UUID uuid;
	private final Action action;

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
		return 0x3e;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeByte(this.action.getID());
		bytes.writeVarInt(1);
		bytes.writeUUID(this.uuid);
		this.action.writeTo(bytes);
		return bytes.getBytes();
	}

	public interface Action {
		byte getID();

		void writeTo(ByteBufWrapper wrapper);
	}

	/**
	 * Adds a player to the tab list
	 */
	@Data
	@AllArgsConstructor
	public static class ActionAddPlayer implements Action {

		private final String name, textures;

		public ActionAddPlayer(String name) {
			this(name, null);
		}

		@Override
		public byte getID() {
			return 0x1;
		}

		@Override
		public void writeTo(ByteBufWrapper wrapper) {
			wrapper.writeString(this.name);

			if (this.textures != null) {
				wrapper.writeVarInt(1);
				wrapper.writeString("textures");
				wrapper.writeString(this.textures);
				wrapper.writeBoolean(false);
			} else {
				wrapper.writeVarInt(0);
			}
		}
	}

	/**
	 * Updates a player's visibility in the tab list
	 */
	@Data
	public static class ActionUpdateListed implements Action {

		private final boolean listed;

		@Override
		public byte getID() {
			return 0x8;
		}

		@Override
		public void writeTo(ByteBufWrapper wrapper) {
			wrapper.writeBoolean(this.listed);
		}
	}
}
