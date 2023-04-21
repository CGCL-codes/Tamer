    public static int write(RandomAccessFile dest, CharBuffer src, ByteOrder order) throws IOException {
        return FileChannels.write(dest.getChannel(), src, order);
    }
