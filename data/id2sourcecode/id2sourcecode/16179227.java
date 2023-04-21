    public static long transferBytesTo(RandomAccessFile src, DataOutput dest, long maxCount) throws IOException {
        return FileChannels.transferBytesTo(src.getChannel(), dest, maxCount);
    }
