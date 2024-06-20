package app.prismarine.server.net.packet.play.in;

import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.util.ByteBufWrapper;
import lombok.Data;

@Data
public class PacketPlayInSwingArm implements Packet {

    boolean offHand;

    public PacketPlayInSwingArm(ByteBufWrapper bytes) {
        this.offHand = bytes.readVarInt() == 1;
    }

    /**
     * @return The direction of the packet - either client -> server (in), or vice versa
     * @see PacketDirection
     */
    @Override
    public PacketDirection getDirection() {
        return PacketDirection.IN;
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
        return 0x36;
    }

    /**
     * @return The packet in raw, serialized form
     */
    @Override
    public byte[] serialize() {
        ByteBufWrapper bytes = new ByteBufWrapper();
        bytes.writeVarInt(this.offHand ? 1 : 0);
        return bytes.getBytes();
    }
}
