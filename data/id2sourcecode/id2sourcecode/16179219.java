    public static void readFully(RandomAccessFile src, LongBuffer dest) throws IOException {
        FileChannels.readFully(src.getChannel(), dest);
    }
