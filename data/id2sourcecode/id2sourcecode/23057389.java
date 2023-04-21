    private final long generateSeed_CRC32(int x, int y) {
        crc32.reset();
        crc32.updateInt(x);
        crc32.updateInt(y);
        return crc32.getValue();
    }
