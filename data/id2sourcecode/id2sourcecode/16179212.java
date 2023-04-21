    public static void readFully(RandomAccessFile src, CharBuffer dest, ByteOrder order) throws IOException {
        FileChannels.readFully(src.getChannel(), dest, order);
    }
