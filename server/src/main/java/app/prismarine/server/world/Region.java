package app.prismarine.server.world;

import app.prismarine.server.PrismarineServer;
import app.prismarine.server.util.BinaryUtil;
import app.prismarine.server.util.ByteBufWrapper;
import lombok.Getter;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;

/**
 * Represents a region in the Anvil save format
 * <p>
 * <a href="https://wiki.vg/Region_Files">Region Files documentation at wiki.vg</a>
 * @author chloe
 */
@Getter
public class Region {

    /**
     * The size, in bytes, of a table in the region file
     */
    private static final int TABLE_SIZE = 4096;

    /**
     * The size, in bytes, of a chunk header in the region file
     */
    private static final int CHUNK_HEADER_SIZE = 5;

    /**
     * The size, in bytes, of a single disk sector, used in the region file
     */
    private static final int SECTOR_SIZE = 4096;

    /**
     * The coordinates of this region
     */
    private final int x, z;

    /**
     * The directory containing this region on disk
     */
    private final File regionsDirectory;

    /**
     * The data of this region on disk
     */
    private final byte[] data;
    private final ByteBufWrapper wrappedData = new ByteBufWrapper();

    /**
     * The {@link LocationTable} of this region file
     */
    private final LocationTable locationTable;

    /**
     * The {@link TimestampTable} of this region file
     */
    private final TimestampTable timestampTable;


    /**
     * Constructs a new region given the coordinates of a chunk within it, and the directory containing it on disk
     * @param chunkX The x-coordinate of a chunk within this region
     * @param chunkZ The z-coordinate of a chunk within this region
     * @param regionsDirectory The directory containing this region on disk
     */
    public Region(int chunkX, int chunkZ, @NotNull File regionsDirectory) {
        this.x = (int) Math.floor(chunkX / 32d);
        this.z = (int) Math.floor(chunkZ / 32d);
        this.regionsDirectory = regionsDirectory;

        try {
            // Read the region data from disk
            wrappedData.writeBytes(Files.readAllBytes(this.getFile().toPath()));
        } catch (Exception e) {
            PrismarineServer.LOGGER.error("Failed to read region at {}", this.getFileName(), e);
        }

        this.data = wrappedData.getBytes();

        // Read the location and timestamp tables
        this.locationTable = new LocationTable(this.wrappedData);
        this.timestampTable = new TimestampTable(this.wrappedData);
    }

    /**
     * Gets the name of this region on disk
     * @return The name of the file on disk representing this region
     */
    public String getFileName() {
        return String.format("r.%d.%d.mca", this.x, this.z);
    }

    /**
     * Gets the file of this region on disk
     * @return The file on disk representing this region
     */
    public File getFile() {
        return new File(regionsDirectory, this.getFileName());
    }

    /**
     * Calculates of position, in bytes, at which a given chunk resides in this region file
     * @param chunk The chunk to analyze
     * @return The byte offset of the chunk in this region file
     */
    public int getChunkOffset(@NotNull Chunk chunk) {
        return this.locationTable.getChunkOffset(chunk);
    }

    /**
     * Calculates the size, in bytes, of a given chunk in the region file
     * @param chunk The chunk to analyze
     * @return The byte size of the chunk in the region file
     */
    public int getChunkSize(@NotNull Chunk chunk) {
        return this.locationTable.getChunkSize(chunk);
    }

    /**
     * Reads the compressed bytes of a chunk belonging to this region.
     * @param chunk The chunk to read
     * @return The compressed bytes of the chunk, <b>including the header</b>
     */
    public byte[] readPackedChunk(@NotNull Chunk chunk) {
        int chunkOffset = this.getChunkOffset(chunk);
        int chunkSize = this.getChunkSize(chunk);

        this.wrappedData.getByteBuf().readerIndex(chunkOffset);
        return this.wrappedData.readBytes(chunkSize);
    }

    /**
     * A representation of the location table in the region file
     */
    private static class LocationTable {

        /**
         * The bytes of the location table
         */
        private final byte[] bytes;

        /**
         * Construct a new LocationTable provided the file bytes, of which {@link Region#TABLE_SIZE} bytes are read
         * @param bytes The file bytes, as a {@link ByteBufWrapper}
         */
        public LocationTable(@NotNull ByteBufWrapper bytes) {
            this.bytes = bytes.readBytes(TABLE_SIZE);
        }

        /**
         * Calculates of position, in bytes, at which a given chunk resides in the region file
         * @param chunk The chunk to analyze
         * @return The byte offset of the chunk in the region file
         */
        public int getChunkOffset(@NotNull Chunk chunk) {
            int chunkOffset = 0;
            int entry = 4 * ((chunk.getX() & 31) + (chunk.getZ() & 31) * 32);

            chunkOffset |= BinaryUtil.readUnsigned(bytes[entry]) << 16;
            chunkOffset |= BinaryUtil.readUnsigned(bytes[entry + 1]) << 8;
            chunkOffset |= BinaryUtil.readUnsigned(bytes[entry + 2]);
            return chunkOffset * SECTOR_SIZE;
        }

        /**
         * Calculates the size, in bytes, of a given chunk in the region file
         * @param chunk The chunk to analyze
         * @return The byte size of the chunk in the region file
         */
        public int getChunkSize(@NotNull Chunk chunk) {
            int entry = 4 * ((chunk.getX() & 31) + (chunk.getZ() & 31) * 32);
            return BinaryUtil.readUnsigned(this.bytes[entry + 3]) * SECTOR_SIZE;
        }
    }

    /**
     * A representation of the timestamp table in the region file
     */
    private static class TimestampTable {

        /**
         * The bytes of the timestamp table
         */
        private final byte[] bytes;

        /**
         * Construct a new TimestampTable provided the file bytes, of which {@link Region#TABLE_SIZE} bytes are read
         * @param bytes The file bytes, as a {@link ByteBufWrapper}
         */
        public TimestampTable(@NotNull ByteBufWrapper bytes) {
            this.bytes = bytes.readBytes(TABLE_SIZE);
        }
    }
}
