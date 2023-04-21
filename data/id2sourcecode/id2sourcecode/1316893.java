    public static long transferFrom(final InputStream src, final FileChannel dest, final long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        if (maxCount == 0) return 0;
        return transferFromByteChannel(IOChannels.asReadableByteChannel(src), dest, maxCount);
    }
