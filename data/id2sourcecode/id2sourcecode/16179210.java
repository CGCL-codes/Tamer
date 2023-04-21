    public static void readFully(RandomAccessFile src, ByteBuffer dest) throws IOException {
        FileChannels.readFully(src.getChannel(), dest);
    }
