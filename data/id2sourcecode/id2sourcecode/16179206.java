    public static void copy(RandomAccessFile source, long sourceIndex, RandomAccessFile dest, long destIndex, long length) throws IOException {
        FileChannels.copy(source.getChannel(), sourceIndex, dest.getChannel(), destIndex, length);
    }
