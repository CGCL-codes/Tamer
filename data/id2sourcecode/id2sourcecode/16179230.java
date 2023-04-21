    public static int write(final RandomAccessFile dest, final CharBuffer src) throws IOException {
        return FileChannels.write(dest.getChannel(), src);
    }
