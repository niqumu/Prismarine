package app.prismarine.server;

/**
 * The entry point for the Prismarine server
 */
public class Main {

    /**
     * Entry point for Prismarine
     * @param args Command line args
     */
    public static void main(String[] args) {
        PrismarineServer.LOGGER.info("Starting!");
        new PrismarineServer().startup();
    }
}
