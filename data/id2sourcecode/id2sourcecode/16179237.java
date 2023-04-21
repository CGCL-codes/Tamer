    public static int write(RandomAccessFile dest, IntBuffer src, ByteOrder order) throws IOException {
        return FileChannels.write(dest.getChannel(), src, order);
    }
