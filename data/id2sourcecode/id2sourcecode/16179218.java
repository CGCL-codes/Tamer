    public static void readFully(RandomAccessFile src, IntBuffer dest, ByteOrder order) throws IOException {
        FileChannels.readFully(src.getChannel(), dest, order);
    }
