    private FileChannel getChannel() throws FileNotFoundException {
        if (channel == null) {
            channel = new RandomAccessFile(file, mode).getChannel();
        }
        return channel;
    }
