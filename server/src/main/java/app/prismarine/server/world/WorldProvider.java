package app.prismarine.server.world;

import app.prismarine.server.PrismarineServer;
import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

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
        if (generate) {
            throw new UnsupportedOperationException("Chunk generation isn't ready yet!");
        }

        Region chunkRegion = new Region(chunk.getX(), chunk.getZ(), this.regionsDirectory);

        byte[] chunkWithHeader = chunkRegion.readPackedChunk(chunk);
        byte[] compressedChunkBytes = Arrays.copyOfRange(chunkWithHeader, 5, chunkWithHeader.length);
        byte[] chunkBytes;
        CompoundTag chunkNBT;

        int chunkCompression = chunkWithHeader[4];

        switch (chunkCompression) {
            case 1 -> { // GZIP
                // todo gzip
                return false;
            }
            case 2 -> { // ZLib
                Inflater inflater = new Inflater();
                inflater.setInput(compressedChunkBytes);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[10000];

                try {
                    while (!inflater.finished()) {
                        int decompressedSize = inflater.inflate(buffer);
                        outputStream.write(buffer, 0, decompressedSize);
                    }
                } catch (Exception e) {
                    PrismarineServer.LOGGER.error("Failed to decompress ZLib compressed chunk {}!", chunk, e);
                    return false;
                }

                chunkBytes = outputStream.toByteArray();
            }
            case 3 -> chunkBytes = compressedChunkBytes; // Uncompressed
            default -> {
                PrismarineServer.LOGGER.error("Encountered unknown chunk compression schema {} in chunk {}!",
                    chunkCompression, chunk);
                return false;
            }
        }

        try {
            // Attempt to read the chunk NBT
            chunkNBT = PrismarineServer.NBT.fromByteArray(chunkBytes);
        } catch (Exception e) {
            PrismarineServer.LOGGER.error("Failed to decode chunk {}!", chunk, e);
            return false;
        }

        // Load the sections from the chunk NBT
        chunk.setSections(new ArrayList<>());
        chunkNBT.getList("sections").forEach(section ->
            chunk.getSections().add(new ChunkSection(chunk, (CompoundTag) section)));

        return true;
    }

    public boolean saveChunk(PrismarineChunk chunk) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
