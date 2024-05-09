package app.prismarine.api;

import lombok.Data;

/**
 * Represents a location in a world
 */
@Data
public class Location {

	private double x, y, z;
	private float yaw, pitch;

	public Location(double x, double y, double z) {
		this(x, y, z, 0f, 0f);
	}

	public Location(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
}
