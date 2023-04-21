    public static long transferToByteChannel(final RandomAccessFile src, final WritableByteChannel dest, final long maxCount) throws IOException {
        return FileChannels.transferToByteChannel(src.getChannel(), dest, maxCount);
    }
