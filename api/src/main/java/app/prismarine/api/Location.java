package app.prismarine.api;

import lombok.Data;

/**
 * Represents a location in a world
 */
@Data
public class Location {

	private double x, y, z;
	private float yaw, pitch;

	/**
	 * Creates a new Location without rotation data
	 * @param x The x position of the new location
	 * @param y The y position of the new location
	 * @param z The z position of the new location
	 */
	public Location(double x, double y, double z) {
		this(x, y, z, 0f, 0f);
	}

	/**
	 * Creates a new Location with rotation data
	 * @param x The x position of the new location
	 * @param y The y position of the new location
	 * @param z The z position of the new location
	 * @param yaw The yaw of the new location
	 * @param pitch The pitch of the new location
	 */
	public Location(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
}
