package app.prismarine.server;

import app.prismarine.server.world.PrismarineWorld;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ServerTickManager;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Simple ServerTickManager implementation
 * @author chloe
 */
public class PrismarineTickManager implements ServerTickManager {

    /**
     * The number of ticks-per-second that this tick manager is currently targeting
     */
    private float tickRate = 20;

    private ServerTickState lastTickState = ServerTickState.HALTED;

    /**
     * The current {@link ServerTickState} of this tick manager
     */
    private ServerTickState tickState = ServerTickState.HALTED;

    /**
     * The number of ticks left to sprint
     */
    private int sprintTicksRemaining = 0;

    /**
     * The number of ticks left to step
     */
    private int stepTicksRemaining = 0;

    /**
     * Starts ticking, setting the tick manager to normal operation
     */
    public void start() {
        if (this.tickState.equals(ServerTickState.HALTED)) {
            this.tickState = ServerTickState.NORMAL;
            this.lastTickState = ServerTickState.HALTED;
            new TickThread().start();
        }
    }

    /**
     * Stops ticking, halting the tick manager.
     */
    public void stop() {
        this.tickState = ServerTickState.HALTED;
    }

    /**
     * Checks if the server is running normally.
     * <p>
     * When the server is running normally it indicates that the server is not
     * currently frozen.
     *
     * @return true if the server is running normally, otherwise false
     */
    @Override
    public boolean isRunningNormally() {
        return this.tickState.equals(ServerTickState.NORMAL);
    }

    /**
     * Checks if the server is currently stepping.
     *
     * @return true if stepping, otherwise false
     */
    @Override
    public boolean isStepping() {
        return this.tickState.equals(ServerTickState.STEPPING);
    }

    /**
     * Checks if the server is currently sprinting.
     *
     * @return true if sprinting, otherwise false
     */
    @Override
    public boolean isSprinting() {
        return this.tickState.equals(ServerTickState.SPRINTING);
    }

    /**
     * Checks if the server is currently frozen.
     *
     * @return true if the server is frozen, otherwise false
     */
    @Override
    public boolean isFrozen() {
        return this.tickState.equals(ServerTickState.FROZEN);
    }

    /**
     * Gets the current tick rate of the server.
     *
     * @return the current tick rate of the server
     */
    @Override
    public float getTickRate() {
        return this.tickRate;
    }

    /**
     * Sets the tick rate of the server.
     * <p>
     * The normal tick rate of the server is 20. No tick rate below 1.0F or
     * above 10,000 can be applied to the server.
     *
     * @param tick the tick rate to set the server to
     * @throws IllegalArgumentException if tick rate is too low or too high for
     *                                  the server to handle
     */
    @Override
    public void setTickRate(float tick) {
        if (tick < 1 || tick > 10000) {
            throw new IllegalArgumentException("Tick speed must be within [1, 10,000]");
        }

        this.tickRate = tick;
    }

    /**
     * Sets the server to a frozen state that does not tick most things.
     *
     * @param frozen true to freeze the server, otherwise false
     */
    @Override
    public void setFrozen(boolean frozen) {
        this.lastTickState = this.tickState;
        this.tickState = frozen ? ServerTickState.FROZEN : ServerTickState.NORMAL;
    }

    /**
     * Steps the game a certain amount of ticks if the server is currently
     * frozen.
     * <p>
     * Steps occur when the server is in a frozen state which can be started by
     * either using the in game /tick freeze command or the
     * {@link #setFrozen(boolean)} method.
     *
     * @param ticks the amount of ticks to step the game for
     * @return true if the game is now stepping. False if the game is not frozen
     * so the request could not be fulfilled.
     */
    @Override
    public boolean stepGameIfFrozen(int ticks) {
        if (!this.tickState.equals(ServerTickState.FROZEN)) {
            return false;
        }

        this.stepTicksRemaining = ticks;
        this.lastTickState = this.tickState;
        this.tickState = ServerTickState.STEPPING;
        return true;
    }

    /**
     * Stops the current stepping if stepping is occurring.
     *
     * @return true if the game is no-longer stepping. False if the server was
     * not stepping or was already done stepping.
     */
    @Override
    public boolean stopStepping() {
        this.lastTickState = this.tickState;
        this.tickState = ServerTickState.NORMAL;
        return this.lastTickState.equals(ServerTickState.STEPPING);
    }

    /**
     * Attempts to initiate a sprint, which executes all server ticks at a
     * faster rate then normal.
     *
     * @param ticks the amount of ticks to sprint for
     * @return true if a sprint was already initiated and was stopped, otherwise
     * false
     */
    @Override
    public boolean requestGameToSprint(int ticks) {
        this.lastTickState = this.tickState;
        this.tickState = ServerTickState.SPRINTING;
        this.sprintTicksRemaining = ticks;
        return this.lastTickState.equals(ServerTickState.SPRINTING);
    }

    /**
     * Stops the current sprint if one is currently happening.
     *
     * @return true if the game is no-longer sprinting, false if the server was
     * not sprinting or was already done sprinting
     */
    @Override
    public boolean stopSprinting() {
        this.lastTickState = tickState;
        this.tickState = ServerTickState.NORMAL;
        return this.lastTickState.equals(ServerTickState.SPRINTING);
    }

    /**
     * Checks if a given entity is frozen.
     *
     * @param entity the entity to check if frozen.
     * @return true if the entity is currently frozen otherwise false.
     */
    @Override
    public boolean isFrozen(@NotNull Entity entity) {
        return this.isFrozen(); // todo: how does this work?
    }

    /**
     * Gets the amount of frozen ticks left to run.
     *
     * @return the amount of frozen ticks left to run
     */
    @Override
    public int getFrozenTicksToRun() {
        return 0; // todo: I have zero idea what this is meant to mean. Does it mean the step ticks left to run...?
    }

    private enum ServerTickState {

        /**
         * The server is not running at all. This is the default state of the tick manager.
         */
        HALTED,

        /**
         * The server is operating normally, ticking {@link PrismarineTickManager#tickRate} times per second.
         */
        NORMAL,

        /**
         * The server is currently frozen and is not ticking. In this state, the server can be stepped or be made
         * to sprint, but it will remain frozen once this completes.
         */
        FROZEN,

        /**
         * Runs a set number of ticks normally at {@link PrismarineTickManager#tickRate} times per second, returning to
         * the frozen state afterwords. Switching to this state is only possible from the frozen state.
         */
        STEPPING,

        /**
         * Runs a set number of ticks at the highest possible speed, without any delay between ticks, returning
         * to the last state afterwords.
         */
        SPRINTING
    }

    private class TickThread extends Thread {

        @Override
        @SneakyThrows
        public void run() {

            // Loop forever as long as the tick manager isn't halted
            while (!tickState.equals(ServerTickState.HALTED)) {
                switch (tickState) {
                    case NORMAL -> this.tickAndWait();
                    case FROZEN -> Thread.sleep((long) (1000 / tickRate)); // Do nothing this tick
                    case STEPPING -> {

                        // If we're done stepping
                        if (stepTicksRemaining <= 0) {
                            tickState = lastTickState;
                            return;
                        }

                        this.tickAndWait();
                        stepTicksRemaining--;
                    }
                    case SPRINTING -> {

                        // If we're done sprinting
                        if (sprintTicksRemaining <= 0) {
                            tickState = lastTickState;
                            return;
                        }

                        this.tick();
                        sprintTicksRemaining--;
                    }
                }
            }
        }

        /**
         * Ticks and waits until the next tick should occur
         */
        @SneakyThrows
        private void tickAndWait() {
            long expectedTickTime = (long) (1000 / tickRate);
            long tickStartTime = System.currentTimeMillis();

            this.tick();

            long elapsedTickTime = System.currentTimeMillis() - tickStartTime;
            Thread.sleep(Math.max(0, expectedTickTime - elapsedTickTime));
        }

        /**
         * Execute a server tick
         */
        private void tick() {
            Bukkit.getServer().getWorlds().forEach(world -> ((PrismarineWorld) world).tick());
        }
    }
}
