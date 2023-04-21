    public static void fill(RandomAccessFile f, byte e, long count) throws IOException {
        FileChannels.fill(f.getChannel(), e, count);
    }
