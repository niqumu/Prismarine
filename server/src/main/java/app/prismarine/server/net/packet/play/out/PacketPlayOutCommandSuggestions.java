package app.prismarine.server.net.packet.play.out;

import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketDirection;
import app.prismarine.server.util.ByteBufWrapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class PacketPlayOutCommandSuggestions implements Packet {

    private final int transactionID;
    private final int start;
    private final int length;
    private final List<Match> matches;

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
        return 0x10;
    }

    /**
     * @return The packet in raw, serialized form
     */
    @Override
    public byte[] serialize() {
        ByteBufWrapper bytes = new ByteBufWrapper();
        bytes.writeVarInt(this.transactionID);
        bytes.writeVarInt(this.start);
        bytes.writeVarInt(this.length);
        bytes.writeVarInt(this.matches.size());
        this.matches.forEach(match -> match.writeTo(bytes));
        return bytes.getBytes();
    }

    public record Match(String text) {

        public void writeTo(ByteBufWrapper buffer) {
            buffer.writeString(this.text);
            buffer.writeBoolean(false);
        }
    }
}
