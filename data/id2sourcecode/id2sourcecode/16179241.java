    public static int write(RandomAccessFile dest, ShortBuffer src, ByteOrder order) throws IOException {
        return FileChannels.write(dest.getChannel(), src, order);
    }
