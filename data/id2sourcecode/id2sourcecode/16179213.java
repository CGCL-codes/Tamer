    public static void readFully(RandomAccessFile src, DoubleBuffer dest) throws IOException {
        FileChannels.readFully(src.getChannel(), dest);
    }
