    public static long transferFromByteChannel(ReadableByteChannel src, RandomAccessFile dest, long maxCount) throws IOException {
        return FileChannels.transferFromByteChannel(src, dest.getChannel(), maxCount);
    }
