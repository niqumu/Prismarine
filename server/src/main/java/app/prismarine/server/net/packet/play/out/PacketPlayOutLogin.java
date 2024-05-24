package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

@Data
@AllArgsConstructor
public class PacketPlayOutLogin implements Packet {

	private final Player player;

	public PacketPlayOutLogin(ByteBufWrapper bytes) {
		throw new UnsupportedOperationException("Attempting to decode outbound packet!");
	}

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
		return 0x2b;
	}

	/**
	 * @return The packet in raw, serialized form
	 */
	@Override
	public byte[] serialize() {
		ByteBufWrapper bytes = new ByteBufWrapper();

		bytes.writeInt(player.getEntityId());

		bytes.writeBoolean(false);
//		bytes.writeBoolean(player.getWorld().isHardcore());

		bytes.writeVarInt(3);
		bytes.writeIdentifier("minecraft", "overworld");
		bytes.writeIdentifier("minecraft", "the_nether");
		bytes.writeIdentifier("minecraft", "the_end");

		// Server values
		bytes.writeVarInt(Bukkit.getServer().getMaxPlayers());
		bytes.writeVarInt(Bukkit.getServer().getViewDistance());
		bytes.writeVarInt(Bukkit.getServer().getSimulationDistance());

		bytes.writeBoolean(false);
		bytes.writeBoolean(true);
		bytes.writeBoolean(false);
		bytes.writeVarInt(0);
		bytes.writeIdentifier("minecraft", "overworld");
		bytes.writeLong((new Random()).nextLong());
		bytes.writeByte(-1);
		bytes.writeByte(3);
		bytes.writeBoolean(false);
		bytes.writeBoolean(true);
		bytes.writeBoolean(false);
		bytes.writeVarInt(0);
		bytes.writeBoolean(false);

		return bytes.getBytes();
	}
}
