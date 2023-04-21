    public synchronized boolean acquire() throws ReplicatorException {
        if (isLocked()) return true;
        try {
            raf = new RandomAccessFile(lockFile, "rw");
            FileChannel channel = raf.getChannel();
            lock = channel.tryLock();
        } catch (FileNotFoundException e) {
            throw new ReplicatorException("Unable to find or create lock file: " + lockFile.getAbsolutePath());
        } catch (Exception e) {
            throw new ReplicatorException("Error while attempting to acquire file lock: " + lockFile.getAbsolutePath(), e);
        } finally {
            if (lock == null && raf != null) {
                close(raf);
            }
        }
        if (lock == null) {
            if (raf != null) close(raf);
            return false;
        } else if (lock.isShared()) {
            release();
            return false;
        } else return true;
    }
