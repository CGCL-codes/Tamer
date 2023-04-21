    public static void readFully(RandomAccessFile src, FloatBuffer dest, ByteOrder order) throws IOException {
        FileChannels.readFully(src.getChannel(), dest, order);
    }
