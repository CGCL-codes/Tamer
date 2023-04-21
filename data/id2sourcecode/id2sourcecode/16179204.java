    public static void clear(RandomAccessFile f, long fromIndex, long count) throws IOException {
        FileChannels.clear(f.getChannel(), fromIndex, count);
    }
