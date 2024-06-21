package app.prismarine.server.world;

import lombok.Getter;

import java.io.File;

public class WorldProvider {
    private final File worldDirectory;

    private final File regionsDirectory;

    public WorldProvider(File worldDirectory) {
        this.worldDirectory = worldDirectory;
        this.regionsDirectory = new File(worldDirectory, "region");
    }

    public boolean loadChunk(PrismarineChunk chunk) {
        return false; // TODO
    }

    public boolean saveChunk(PrismarineChunk chunk) {
        return false; // TODO
    }

    @Getter
    private class Region {

        private final int x, z;

        public Region(int chunkX, int chunkZ) {
            this.x = (int) Math.floor(chunkX / 32d);
            this.z = (int) Math.floor(chunkZ / 32d);
        }

        public String getFileName() {
            return String.format("r.%d.%d.mcr", this.x, this.z);
        }

        public File getFile() {
            return new File(regionsDirectory, this.getFileName());
        }
    }
}
