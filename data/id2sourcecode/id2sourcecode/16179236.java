    public static int write(RandomAccessFile dest, IntBuffer src) throws IOException {
        return FileChannels.write(dest.getChannel(), src);
    }
