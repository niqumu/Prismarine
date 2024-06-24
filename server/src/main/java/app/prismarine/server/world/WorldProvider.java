package app.prismarine.server.world;

import java.io.File;

/**
 * Class responsible for handling the abstraction of worlds from their on-disk storage
 * @author chloe
 */
public class WorldProvider {

    /**
     * The top-level directory of the world
     */
    private final File worldDirectory;

    /**
     * The regions directory of the world
     */
    private final File regionsDirectory;

    /**
     * Construct a new WorldProvider given the top-level directory of the world
     * @param worldDirectory The top-level directory of the world
     */
    public WorldProvider(File worldDirectory) {
        this.worldDirectory = worldDirectory;
        this.regionsDirectory = new File(worldDirectory, "region");
    }

    public boolean loadChunk(PrismarineChunk chunk, boolean generate) {
        Region chunkRegion = new Region(chunk.getX(), chunk.getZ(), this.regionsDirectory);

        return false; // TODO
    }

    public boolean saveChunk(PrismarineChunk chunk) {
        return false; // TODO
    }
}
