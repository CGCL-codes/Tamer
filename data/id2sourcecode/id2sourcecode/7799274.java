    public static final synchronized boolean tryLock(File location) {
        if (location == null) {
            location = getDataFolder();
        }
        if (exclusiveLock != null) {
            return false;
        }
        try {
            FileChannel lock = new RandomAccessFile(new File(location, LOCK_FILE_NAME), "rw").getChannel();
            exclusiveLock = lock.tryLock();
        } catch (Exception e) {
            return false;
        }
        return exclusiveLock != null;
    }
