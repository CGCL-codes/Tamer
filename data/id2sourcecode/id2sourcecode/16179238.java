    public static int write(RandomAccessFile dest, LongBuffer src) throws IOException {
        return FileChannels.write(dest.getChannel(), src);
    }
