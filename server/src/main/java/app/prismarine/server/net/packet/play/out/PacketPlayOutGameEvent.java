package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class PacketPlayOutGameEvent implements Packet {

	private final Event event;
	private final float data;

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
		return 0x22;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();
		bytes.writeByte((byte) this.event.getId());
		bytes.writeFloat(data);
		return bytes.getBytes();
	}

	@Getter
	@AllArgsConstructor
	public enum Event {
		NO_RESPAWN(0),
		END_RAIN(1),
		BEGIN_RAIN(2),
		CHANGE_GAME_MODE(3),
		WIN_GAME(4),
		DEMO_EVENT(5),
		ARROW_HIT(6),
		RAIN_LEVEL_CHANGE(7),
		THUNDER_LEVEL_CHANGE(8),
		PUFFERFISH_STING(9),
		ELDER_GUARDIAN_APPEAR(10),
		ENABLE_RESPAWN_SCREEN(11),
		LIMITED_CRAFTING(12),
		START_WAITING_FOR_CHUNKS(13);

		private final int id;
	}
}
