    private void open() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(lockFile, "rw");
        channel = raf.getChannel();
    }
