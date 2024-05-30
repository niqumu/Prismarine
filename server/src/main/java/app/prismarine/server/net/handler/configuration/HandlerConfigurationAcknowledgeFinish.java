package app.prismarine.server.net.handler.configuration;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.ConnectionState;
import app.prismarine.server.net.packet.Packet;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.configuration.PacketConfigurationInAcknowledgeFinish;
import app.prismarine.server.net.packet.play.out.*;
import app.prismarine.server.util.HTTPUtil;
import app.prismarine.server.world.PrismarineChunk;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class HandlerConfigurationAcknowledgeFinish implements PacketHandler<PacketConfigurationInAcknowledgeFinish> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketConfigurationInAcknowledgeFinish packet) {
		connection.setState(ConnectionState.PLAY);

		// Send login packet
		connection.sendPacket(new PacketPlayOutLogin(connection.getPlayer()));

		// Set the player position
		connection.sendPacket(new PacketPlayOutSyncPlayerPosition(0, 64, 0, 0, 0));

		// Fetch player textures
		String url = "https://sessionserver.mojang.com/session/minecraft/profile/" +
			connection.getPlayer().getUniqueId();

		String response = HTTPUtil.get(url).response();
		JsonObject parsedResponse = JsonParser.parseString(response).getAsJsonObject();
		JsonArray properties = parsedResponse.getAsJsonArray("properties");
		String textures = null;

		for (JsonElement propertyElement : properties) {
			JsonObject property = propertyElement.getAsJsonObject();

			if (property.get("name").getAsString().equals("textures")) {
				textures = property.get("value").toString();
				textures = textures.substring(1, textures.length() - 1); // thanks mojang!
			}
		}

		// Send player info
		Packet addPlayer = new PacketPlayOutPlayerInfoUpdate(connection.getPlayer().getUniqueId(),
			new PacketPlayOutPlayerInfoUpdate.ActionAddPlayer(connection.getPlayer().getName(), textures));
		connection.getNettyServer().broadcastPacket(addPlayer);

		Packet showPlayer = new PacketPlayOutPlayerInfoUpdate(connection.getPlayer().getUniqueId(),
			new PacketPlayOutPlayerInfoUpdate.ActionUpdateListed(true));
		connection.getNettyServer().broadcastPacket(showPlayer);

		// Create and fire a new player join event
		PlayerJoinEvent event = ((PrismarineServer) Bukkit.getServer()).
			getEventManager().onPlayerJoin(connection.getPlayer());

		// Broadcast the join message if one exists
		if (event.getJoinMessage() != null) {
			Bukkit.getServer().broadcastMessage(event.getJoinMessage());
		}

		// Prepare for the player's world to send chunks
		connection.sendPacket(new PacketPlayOutGameEvent(PacketPlayOutGameEvent.Event.START_WAITING_FOR_CHUNKS, 0));

		// TODO testing
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				connection.sendPacket(new PacketPlayOutChunkData(new PrismarineChunk(x, z, null)));
			}
		}
	}
}
