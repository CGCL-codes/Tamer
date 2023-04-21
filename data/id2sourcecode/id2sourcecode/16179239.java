    public static int write(RandomAccessFile dest, LongBuffer src, ByteOrder order) throws IOException {
        return FileChannels.write(dest.getChannel(), src, order);
    }
