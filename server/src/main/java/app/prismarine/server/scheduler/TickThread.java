package app.prismarine.server.scheduler;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.world.PrismarineWorld;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A thread that runs on a regular interval to tick the world and entities
 */
public class TickThread extends Thread {

	private static final int TICKS_PER_SECOND = 20;
	private static final int MS_PER_TICK = 1000 / TICKS_PER_SECOND;

	private long startTime;
	private long tickCount = 0;

	private long lastTickTime = 0;

	/**
	 * Whether this tick thread is alive or note
	 */
	private final AtomicBoolean running = new AtomicBoolean(true);

	@Override
	@SneakyThrows
	public void run() {
		this.setName("Tick thread");

		this.startTime = System.currentTimeMillis();

		// Loop as long as the tick thread is set to running
		while (this.running.get()) {

			// Wait until a tick is required
			if (tickCount < getExpectedTick()) {
				this.doTick();
				this.analyzeLastTick();
				this.tickCount++;
			}
		}
	}

	private void doTick() {
		final long tickStartTime = System.currentTimeMillis();

		// tick
//		Bukkit.getServer().getWorlds().forEach(world -> ((PrismarineWorld) world).tick());
		((PrismarineServer) Bukkit.getServer()).getEntityManager().tick();

		this.lastTickTime = System.currentTimeMillis() - tickStartTime;
	}

	private void analyzeLastTick() {

		// If the tick took longer than the expected time, display a warning
		if (this.lastTickTime > MS_PER_TICK) {

			long ticksBehind = this.getExpectedTick() - this.tickCount;

			PrismarineServer.LOGGER.warn("Can't keep up! The last tick took {}ms, and the server is now" +
				" catching up by {} ticks!", this.lastTickTime, ticksBehind);
		}
	}

	private long getExpectedTick() {
		long msRunning = System.currentTimeMillis() - this.startTime;
		return msRunning / MS_PER_TICK;
	}

	/**
	 * Stop the tick thread
	 */
	public void shutdown() {
		this.running.set(false);
	}
}
