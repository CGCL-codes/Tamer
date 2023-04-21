    public synchronized boolean tryLock() throws OverlappingFileLockException {
        if (lock != null) {
            throw new OverlappingFileLockException();
        }
        File lockFile = getLockFile();
        lockFile.getParentFile().mkdirs();
        try {
            RandomAccessFile raf = new RandomAccessFile(lockFile, "rw");
            FileLock l = raf.getChannel().tryLock();
            if (l != null) {
                lock = l;
                return true;
            }
        } catch (IOException ioe) {
            lock = null;
        }
        return false;
    }
