    private static synchronized boolean canLockFile(File lockFile, boolean isReleaseLockAfterTest) throws Exception {
        FileChannel fc = new RandomAccessFile(lockFile, "rw").getChannel();
        FileLock lock = null;
        try {
            lock = fc.tryLock();
            try {
                if (lock == null) {
                    fc.close();
                    return false;
                }
                if (isReleaseLockAfterTest) {
                    lock.release();
                }
            } catch (Exception nope) {
            }
        } catch (IOException ioe) {
            File workAround = new File(lockFile.getParentFile(), "lock-workaround.file");
            if (workAround.exists()) {
                return false;
            }
            if (!workAround.createNewFile()) {
                return false;
            }
            System.out.println("WARNING: Could not open file lock, so using a work-around file \"lock\" at: " + workAround.getAbsolutePath());
            workAround.deleteOnExit();
        }
        return true;
    }
