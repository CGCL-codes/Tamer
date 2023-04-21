    public static void readFully(RandomAccessFile src, ShortBuffer dest, ByteOrder order) throws IOException {
        FileChannels.readFully(src.getChannel(), dest, order);
    }
