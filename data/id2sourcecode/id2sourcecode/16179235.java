    public static int write(RandomAccessFile dest, FloatBuffer src, ByteOrder order) throws IOException {
        return FileChannels.write(dest.getChannel(), src, order);
    }
