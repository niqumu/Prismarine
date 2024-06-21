package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.util.ByteBufWrapper;
import lombok.Data;
import org.bukkit.entity.Entity;

@Data
public class PacketPlayOutEntityHeadYaw implements Packet {

    private final Entity entity;
    private final float yaw;

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
        return 0x48;
    }

    /**
     * @return The packet in raw, serialized form
     */
    @Override
    public byte[] serialize() {
        ByteBufWrapper bytes = new ByteBufWrapper();
        bytes.writeVarInt(this.entity.getEntityId());
        bytes.writeAngle(this.yaw);
        return bytes.getBytes();
    }
}
