    public boolean isAppActive() {
        try {
            file = new File(System.getProperty("user.home"), appName + ".tmp");
            channel = new RandomAccessFile(file, "rw").getChannel();
            try {
                lock = channel.tryLock();
            } catch (OverlappingFileLockException e) {
                closeLock();
                return true;
            }
            if (lock == null) {
                closeLock();
                return true;
            }
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    closeLock();
                    deleteFile();
                }
            });
            return false;
        } catch (Exception e) {
            closeLock();
            return true;
        }
    }
