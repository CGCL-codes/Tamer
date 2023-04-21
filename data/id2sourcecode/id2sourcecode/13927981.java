    private void obtainLock() {
        File lockFile = new File(metadata, APPLICATION_LOCK_FILE);
        try {
            RandomAccessFile raf = new RandomAccessFile(lockFile, "rw");
            lock = raf.getChannel().lock();
        } catch (IOException ioe) {
            lock = null;
        }
    }
