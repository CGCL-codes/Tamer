    public static void readFully(RandomAccessFile src, LongBuffer dest, ByteOrder order) throws IOException {
        FileChannels.readFully(src.getChannel(), dest, order);
    }
