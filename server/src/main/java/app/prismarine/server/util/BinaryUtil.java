package app.prismarine.server.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BinaryUtil {

    public int readUnsigned(byte b) {
        return b & 0xff;
    }
}
