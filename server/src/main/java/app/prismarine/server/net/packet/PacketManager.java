package app.prismarine.server.net.packet;

import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.configuration.*;
import app.prismarine.server.net.packet.handshake.*;
import app.prismarine.server.net.packet.login.*;
import app.prismarine.server.net.packet.status.*;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.HashMap;

@UtilityClass
public class PacketManager {

	private final HashMap<Integer, Class<? extends Packet>> HANDSHAKE_MAPPINGS = new HashMap<>();
	private final HashMap<Integer, Class<? extends Packet>> STATUS_IN_MAPPINGS = new HashMap<>();
	private final HashMap<Integer, Class<? extends Packet>> STATUS_OUT_MAPPINGS = new HashMap<>();
	private final HashMap<Integer, Class<? extends Packet>> LOGIN_IN_MAPPINGS = new HashMap<>();
	private final HashMap<Integer, Class<? extends Packet>> LOGIN_OUT_MAPPINGS = new HashMap<>();
	private final HashMap<Integer, Class<? extends Packet>> CONFIGURATION_IN_MAPPINGS = new HashMap<>();
	private final HashMap<Integer, Class<? extends Packet>> CONFIGURATION_OUT_MAPPINGS = new HashMap<>();
	private final HashMap<Integer, Class<? extends Packet>> PLAY_IN_MAPPINGS = new HashMap<>();
	private final HashMap<Integer, Class<? extends Packet>> PLAY_OUT_MAPPINGS = new HashMap<>();

	public void registerPackets() {

		// Handshake
		register(PacketHandshakeInHandshake.class, ConnectionState.HANDSHAKING, PacketDirection.IN, 0x0);

		// Status
		register(PacketStatusInStatusRequest.class, ConnectionState.STATUS, PacketDirection.IN, 0x0);
		register(PacketStatusOutStatusResponse.class, ConnectionState.STATUS, PacketDirection.OUT, 0x0);
		register(PacketStatusInPingRequest.class, ConnectionState.STATUS, PacketDirection.IN, 0x1);
		register(PacketStatusOutPingResponse.class, ConnectionState.STATUS, PacketDirection.OUT, 0x1);

		// Login
		register(PacketLoginInLoginStart.class, ConnectionState.LOGIN, PacketDirection.IN, 0x0);
		register(PacketLoginOutDisconnect.class, ConnectionState.LOGIN, PacketDirection.OUT, 0x0);

		register(PacketLoginOutSuccess.class, ConnectionState.LOGIN, PacketDirection.OUT, 0x2);

		register(PacketLoginInAcknowledge.class, ConnectionState.LOGIN, PacketDirection.IN, 0x3);

		// Configuration
		register(PacketConfigurationInConfig.class, ConnectionState.CONFIGURATION, PacketDirection.IN, 0x0);

		register(PacketConfigurationOutDisconnect.class, ConnectionState.CONFIGURATION, PacketDirection.OUT, 0x1);
		register(PacketConfigurationInAcknowledgeFinish.class, ConnectionState.CONFIGURATION, PacketDirection.IN, 0x2);
		register(PacketConfigurationOutFinish.class, ConnectionState.CONFIGURATION, PacketDirection.OUT, 0x2);
	}

	private void register(Class<? extends Packet> clazz, ConnectionState state, PacketDirection direction, int id) {
		getMappings(state, direction).put(id, clazz);
	}

	@SneakyThrows
	public Packet build(Class<? extends Packet> clazz, ByteBufWrapper bytes) {
		return clazz.getConstructor(ByteBufWrapper.class).newInstance(bytes);
	}

	public Class<? extends Packet> match(ConnectionState state, PacketDirection direction, int id) {
		HashMap<Integer, Class<? extends Packet>> mappings = getMappings(state, direction);
		return mappings.get(id);
	}

	private HashMap<Integer, Class<? extends Packet>> getMappings(ConnectionState state, PacketDirection direction) {

		boolean out = direction.equals(PacketDirection.OUT);

		if (state.equals(ConnectionState.HANDSHAKING)) {
			return HANDSHAKE_MAPPINGS;
		} else if (state.equals(ConnectionState.STATUS)) {
			return out ? STATUS_OUT_MAPPINGS : STATUS_IN_MAPPINGS;
		} else if (state.equals(ConnectionState.LOGIN)) {
			return out ? LOGIN_OUT_MAPPINGS : LOGIN_IN_MAPPINGS;
		} else {
			return out ? PLAY_OUT_MAPPINGS : PLAY_IN_MAPPINGS;
		}
	}
}
