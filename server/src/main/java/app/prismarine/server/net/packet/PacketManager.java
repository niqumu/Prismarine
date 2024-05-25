package app.prismarine.server.net.packet;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.net.ByteBufWrapper;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.handler.configuration.HandlerConfigurationAcknowledgeFinish;
import app.prismarine.server.net.handler.configuration.HandlerConfigurationConfig;
import app.prismarine.server.net.handler.configuration.HandlerConfigurationPacks;
import app.prismarine.server.net.handler.configuration.HandlerConfigurationPluginMessage;
import app.prismarine.server.net.handler.handshake.HandlerHandshake;
import app.prismarine.server.net.handler.login.HandlerLoginAcknowledge;
import app.prismarine.server.net.handler.login.HandlerLoginStartLogin;
import app.prismarine.server.net.handler.play.*;
import app.prismarine.server.net.handler.status.HandlerStatusPingRequest;
import app.prismarine.server.net.handler.status.HandlerStatusStatusRequest;
import app.prismarine.server.net.packet.configuration.*;
import app.prismarine.server.net.packet.handshake.*;
import app.prismarine.server.net.packet.login.*;
import app.prismarine.server.net.packet.play.in.*;
import app.prismarine.server.net.packet.play.out.*;
import app.prismarine.server.net.packet.status.*;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.HashMap;

import static app.prismarine.server.net.ConnectionState.*;

@UtilityClass
public class PacketManager {

	public final HashMap<Class<? extends Packet>, PacketHandler<Packet>> HANDLERS = new HashMap<>();

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
		registerInbound(PacketHandshakeInHandshake.class, new HandlerHandshake(), HANDSHAKING, 0x0);

		// Status in
		registerInbound(PacketStatusInStatusRequest.class, new HandlerStatusStatusRequest(), STATUS, 0x0);
		registerInbound(PacketStatusInPingRequest.class, new HandlerStatusPingRequest(), STATUS, 0x1);

		// Status out
		registerOutbound(PacketStatusOutStatusResponse.class, STATUS, 0x0);
		registerOutbound(PacketStatusOutPingResponse.class, STATUS, 0x1);

		// Login in
		registerInbound(PacketLoginInLoginStart.class, new HandlerLoginStartLogin(), LOGIN, 0x0);

		registerInbound(PacketLoginInAcknowledge.class, new HandlerLoginAcknowledge(), LOGIN, 0x3);

		// Login out
		registerOutbound(PacketLoginOutDisconnect.class, LOGIN, 0x0);

		registerOutbound(PacketLoginOutSuccess.class, LOGIN, 0x2);

		// Configuration in
		registerInbound(PacketConfigurationInConfig.class, new HandlerConfigurationConfig(), CONFIGURATION, 0x0);

		registerInbound(PacketConfigurationInPluginMessage.class, new HandlerConfigurationPluginMessage(), CONFIGURATION, 0x2);
		registerInbound(PacketConfigurationInAcknowledgeFinish.class, new HandlerConfigurationAcknowledgeFinish(), CONFIGURATION, 0x3);

		registerInbound(PacketConfigurationInPacks.class, new HandlerConfigurationPacks(), CONFIGURATION, 0x7);

		// Configuration out
		registerOutbound(PacketConfigurationOutPluginMessage.class, CONFIGURATION, 0x1);
		registerOutbound(PacketConfigurationOutDisconnect.class, CONFIGURATION, 0x2);
		registerOutbound(PacketConfigurationOutFinish.class, CONFIGURATION, 0x3);

		registerOutbound(PacketConfigurationOutRegistry.class, CONFIGURATION, 0x7);

		registerOutbound(PacketConfigurationOutPacks.class, CONFIGURATION, 0xe);

		// Play in
		registerInbound(PacketPlayInChatMessage.class, new HandlerPlayChatMessage(), PLAY, 0x6);

		registerInbound(PacketPlayInKeepAlive.class, new HandlerPlayKeepAlive(), PLAY, 0x18);

		registerInbound(PacketPlayInPosition.class, new HandlerPlayPosition(), PLAY, 0x1a);
		registerInbound(PacketPlayInPositionRotation.class, new HandlerPlayPositionRotation(), PLAY, 0x1b);
		registerInbound(PacketPlayInRotation.class, new HandlerPlayRotation(), PLAY, 0x1c);

		// Play out
		registerOutbound(PacketPlayOutDisconnect.class, PLAY, 0x1d);

		registerOutbound(PacketPlayOutGameEvent.class, PLAY, 0x22);

		registerOutbound(PacketPlayOutKeepAlive.class, PLAY, 0x26);
		registerOutbound(PacketPlayOutChunkData.class, PLAY, 0x27);

		registerOutbound(PacketPlayOutLogin.class, PLAY, 0x2b);

		registerOutbound(PacketPlayOutPlayerMessage.class, PLAY, 0x39);

		registerOutbound(PacketPlayOutPlayerInfoUpdate.class, PLAY, 0x3e);

		registerOutbound(PacketPlayOutSyncPlayerPosition.class, PLAY, 0x40);

		registerOutbound(PacketPlayOutSyncPlayerPosition.class, PLAY, 0x54);
	}

	/**
	 * Used to register inbound packets, defining a packet handler
	 *
	 * @param clazz   The class of the packet
	 * @param handler An instance of an appropriate packet handler to be called when the packet is received
	 * @param state   The {@link ConnectionState} this packet belongs to
	 * @param id      The internal ID of this packet
	 */
	@SuppressWarnings("unchecked")
	private void registerInbound(Class<? extends Packet> clazz, PacketHandler<? extends Packet> handler,
	                             ConnectionState state, int id) {
		HANDLERS.put(clazz, (PacketHandler<Packet>) handler);
		getMappings(state, PacketDirection.IN).put(id, clazz);
	}

	/**
	 * Used to register outbound packets, as they do not have a packet handler
	 *
	 * @param clazz The class of the packet
	 * @param state The {@link ConnectionState} this packet belongs to
	 * @param id    The internal ID of this packet
	 */
	private void registerOutbound(Class<? extends Packet> clazz, ConnectionState state, int id) {
		getMappings(state, PacketDirection.OUT).put(id, clazz);
	}

	/**
	 * Handle an incoming packet by calling the appropriate {@link PacketHandler}
	 * @param packet The packet that was received by the server
	 */
	public void handle(Connection connection, Packet packet) {
		if (!HANDLERS.containsKey(packet.getClass())) {
			PrismarineServer.LOGGER.warn("Tried to handle packet {} without a handler!", packet);
			return;
		}

		HANDLERS.get(packet.getClass()).handle(connection, packet);
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
		} else if (state.equals(ConnectionState.CONFIGURATION)) {
			return out ? CONFIGURATION_OUT_MAPPINGS : CONFIGURATION_IN_MAPPINGS;
		} else {
			return out ? PLAY_OUT_MAPPINGS : PLAY_IN_MAPPINGS;
		}
	}
}
