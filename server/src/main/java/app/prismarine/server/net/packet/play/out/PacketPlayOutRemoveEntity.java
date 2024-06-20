package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.util.ByteBufWrapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class PacketPlayOutRemoveEntity implements Packet {

    private final List<Entity> entities;

    public PacketPlayOutRemoveEntity(Entity entity) {
        this(new ArrayList<>() {{
            add(entity);
        }});
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
        return 0x42;
    }

    /**
     * @return The packet in raw, serialized form
     */
    @Override
    public byte[] serialize() {
        ByteBufWrapper bytes = new ByteBufWrapper();
        bytes.writeVarInt(this.entities.size());
        this.entities.forEach(entity -> bytes.writeVarInt(entity.getEntityId()));
        return bytes.getBytes();
    }
}
