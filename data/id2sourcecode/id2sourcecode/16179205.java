    public static void copy(RandomAccessFile f, long sourceIndex, long destIndex, long length) throws IOException {
        FileChannels.copy(f.getChannel(), sourceIndex, destIndex, length);
    }
