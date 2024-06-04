package app.prismarine.server.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class MojangUtil {

	public String fetchTextures(Player player) {
		String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + player.getUniqueId();

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

		return textures;
	}
}
