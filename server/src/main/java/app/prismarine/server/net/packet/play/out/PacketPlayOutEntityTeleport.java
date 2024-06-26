package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.util.ByteBufWrapper;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

@Data
public class PacketPlayOutEntityTeleport implements Packet {

    private final Entity entity;
    private final Location location;
    private final boolean onGround;

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
        return 0x70;
    }

    /**
     * @return The packet in raw, serialized form
     */
    @Override
    public byte[] serialize() {
        ByteBufWrapper bytes = new ByteBufWrapper();
        bytes.writeVarInt(this.entity.getEntityId());
        bytes.writeDouble(this.location.getX());
        bytes.writeDouble(this.location.getY());
        bytes.writeDouble(this.location.getZ());
        bytes.writeAngle(this.location.getYaw());
        bytes.writeAngle(this.location.getPitch());
        bytes.writeBoolean(this.onGround);
        return bytes.getBytes();
    }
}
