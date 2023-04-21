    public boolean isApplicationActive() {
        try {
            channel = new RandomAccessFile(file, "rw").getChannel();
            try {
                lock = channel.tryLock();
            } catch (OverlappingFileLockException e) {
                removeFileLock();
                return true;
            }
            if (lock == null) {
                removeFileLock();
                return true;
            }
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    removeFileLock();
                    deleteFile();
                }
            });
            return false;
        } catch (Exception e) {
            removeFileLock();
            return true;
        }
    }
