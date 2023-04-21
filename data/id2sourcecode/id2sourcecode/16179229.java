    public static int write(final RandomAccessFile dest, final ByteBuffer src) throws IOException {
        return FileChannels.write(dest.getChannel(), src);
    }
