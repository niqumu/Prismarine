package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.util.ByteBufWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Entity;

@Data
public class PacketPlayOutEntityAnimation implements Packet {

    private final Entity entity;
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
        return 0x3;
    }

    /**
     * @return The packet in raw, serialized form
     */
    @Override
    public byte[] serialize() {
        ByteBufWrapper bytes = new ByteBufWrapper();
        bytes.writeVarInt(this.entity.getEntityId());
        bytes.writeByte(this.action.getId());
        return bytes.getBytes();
    }

    @Getter
    @AllArgsConstructor
    public enum Action {
        SWING_MAIN(0),
        LEAVE_BED(2),
        SWING_OFFHAND(3),
        CRITICAL_EFFECT(4),
        MAGIC_CRITICAL_EFFECT(5);

        private final int id;
    }
}
