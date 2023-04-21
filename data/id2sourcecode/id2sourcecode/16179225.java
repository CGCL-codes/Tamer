    public static void transferBytesFullyFrom(final DataInput src, RandomAccessFile dest, long count) throws IOException {
        FileChannels.transferBytesFullyFrom(src, dest.getChannel(), count);
    }
