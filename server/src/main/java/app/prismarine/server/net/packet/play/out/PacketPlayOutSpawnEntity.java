package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.util.ByteBufWrapper;
import lombok.Data;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

@Data
public class PacketPlayOutSpawnEntity implements Packet {

    private final Entity entity;

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
        return 0x1;
    }

    /**
     * @return The packet in raw, serialized form
     */
    @Override
    public byte[] serialize() {
        ByteBufWrapper bytes = new ByteBufWrapper();
        bytes.writeVarInt(entity.getEntityId());
        bytes.writeUUID(entity.getUniqueId());
        bytes.writeVarInt(122); // TODO TESTING - this only works with players!!
        bytes.writeDouble(entity.getLocation().getX());
        bytes.writeDouble(entity.getLocation().getY());
        bytes.writeDouble(entity.getLocation().getZ());
        bytes.writeAngle(entity.getLocation().getPitch());
        bytes.writeAngle(entity.getLocation().getYaw());
        bytes.writeAngle(entity instanceof LivingEntity le ? le.getEyeLocation().getYaw() : entity.getLocation().getYaw());
        bytes.writeVarInt(0);
        bytes.writeShort(0);
        bytes.writeShort(0);
        bytes.writeShort(0);
        return bytes.getBytes();
    }
}
