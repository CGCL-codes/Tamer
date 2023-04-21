    public static boolean writeLock() {
        File lockfile = new File(System.getProperty("java.io.tmpdir"), "vanesa.lock");
        try {
            file = new RandomAccessFile(lockfile, "rw");
            f = file.getChannel();
            lock = f.tryLock();
        } catch (IOException e) {
            Logger.getRootLogger().error(e.getMessage(), e);
        }
        if (lock != null) {
            lockfile.deleteOnExit();
            return true;
        } else {
            return false;
        }
    }
