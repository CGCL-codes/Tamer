    public static long transferFrom(InputStream src, RandomAccessFile dest, long maxCount) throws IOException {
        return FileChannels.transferFrom(src, dest.getChannel(), maxCount);
    }
