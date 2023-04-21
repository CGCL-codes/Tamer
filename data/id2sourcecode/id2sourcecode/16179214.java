    public static void readFully(RandomAccessFile src, DoubleBuffer dest, ByteOrder order) throws IOException {
        FileChannels.readFully(src.getChannel(), dest, order);
    }
