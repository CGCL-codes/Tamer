    public static long transferTo(RandomAccessFile src, OutputStream dest, long maxCount) throws IOException {
        return FileChannels.transferTo(src.getChannel(), dest, maxCount);
    }
