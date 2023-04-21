    public static int write(RandomAccessFile dest, ShortBuffer src) throws IOException {
        return FileChannels.write(dest.getChannel(), src);
    }
