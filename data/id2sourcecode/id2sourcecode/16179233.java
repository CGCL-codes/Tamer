    public static int write(RandomAccessFile dest, DoubleBuffer src, ByteOrder order) throws IOException {
        return FileChannels.write(dest.getChannel(), src, order);
    }
