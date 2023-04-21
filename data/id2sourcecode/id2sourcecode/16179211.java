    public static void readFully(RandomAccessFile src, CharBuffer dest) throws IOException {
        FileChannels.readFully(src.getChannel(), dest);
    }
