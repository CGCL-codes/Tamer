    public static int write(RandomAccessFile dest, FloatBuffer src) throws IOException {
        return FileChannels.write(dest.getChannel(), src);
    }
