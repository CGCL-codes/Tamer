    private void lock() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(lockFile, "rw");
        lock = raf.getChannel().lock();
    }
