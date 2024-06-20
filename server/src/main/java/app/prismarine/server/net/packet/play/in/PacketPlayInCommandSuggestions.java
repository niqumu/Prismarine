package app.prismarine.server.net.packet.play.in;

import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.util.ByteBufWrapper;
import lombok.Data;

@Data
public class PacketPlayInCommandSuggestions implements Packet {

    private final int transactionID;
    private final String text;

    public PacketPlayInCommandSuggestions(ByteBufWrapper bytes) {
        this.transactionID = bytes.readVarInt();
        this.text = bytes.readString();
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
        return 0xb;
    }

    /**
     * @return The packet in raw, serialized form
     */
    @Override
    public byte[] serialize() {
        ByteBufWrapper bytes = new ByteBufWrapper();
        bytes.writeVarInt(this.transactionID);
        bytes.writeString(this.text);
        return bytes.getBytes();
    }
}
