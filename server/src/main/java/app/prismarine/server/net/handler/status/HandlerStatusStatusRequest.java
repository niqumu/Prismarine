package app.prismarine.server.net.handler.status;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.net.Connection;
import app.prismarine.server.net.packet.PacketHandler;
import app.prismarine.server.net.packet.status.PacketStatusInStatusRequest;
import app.prismarine.server.net.packet.status.PacketStatusOutStatusResponse;
import com.google.gson.JsonObject;

public class HandlerStatusStatusRequest implements PacketHandler<PacketStatusInStatusRequest> {

	/**
	 * @param connection The connection this packet was received from
	 * @param packet     The packet received by the server to handle
	 */
	@Override
	public void handle(Connection connection, PacketStatusInStatusRequest packet) {

		JsonObject response = new JsonObject();

		// Create the version object
		JsonObject version = new JsonObject();
		version.addProperty("name", PrismarineServer.GAME_VERSION);
		version.addProperty("protocol", PrismarineServer.PROTOCOL_VERSION);
		response.add("version", version);

		// Create the players object
		JsonObject players = new JsonObject();
		players.addProperty("max", PrismarineServer.getServer().getConfig().getMaxPlayers());
		players.addProperty("online", 0); // TODO
		response.add("players", players);

		// Create the description object
		JsonObject description = new JsonObject();
		description.addProperty("text", PrismarineServer.getServer().getConfig().getMotd());
		response.add("description", description);

		response.addProperty("favicon", "data:image/png;base64,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");
		response.addProperty("enforcesSecureChat", false);
		response.addProperty("previewsChat", false);

		connection.sendPacket(new PacketStatusOutStatusResponse(response.toString()));
	}
}
