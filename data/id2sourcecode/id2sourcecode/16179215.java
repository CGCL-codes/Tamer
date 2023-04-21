    public static void readFully(RandomAccessFile src, FloatBuffer dest) throws IOException {
        FileChannels.readFully(src.getChannel(), dest);
    }
