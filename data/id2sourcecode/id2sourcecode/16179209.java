    public static void move(RandomAccessFile source, long sourceIndex, RandomAccessFile dest, long destIndex, long count) throws IOException {
        FileChannels.move(source.getChannel(), sourceIndex, dest.getChannel(), destIndex, count);
    }
