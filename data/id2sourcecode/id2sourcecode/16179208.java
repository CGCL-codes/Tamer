    public static void move(RandomAccessFile f, long sourceIndex, long destIndex, long count) throws IOException {
        FileChannels.move(f.getChannel(), sourceIndex, destIndex, count);
    }
