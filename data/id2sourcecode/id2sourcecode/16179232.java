    public static int write(RandomAccessFile dest, DoubleBuffer src) throws IOException {
        return FileChannels.write(dest.getChannel(), src);
    }
