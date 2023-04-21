    public boolean getFeedLock() {
        String lockName = indexPath + FS + LOCKFILENAME + binder.getId();
        File lf = new File(lockName);
        if (!lf.exists()) {
            try {
                lf.createNewFile();
            } catch (Exception e) {
            }
        }
        try {
            fileChannel = new RandomAccessFile(lf, "rw").getChannel();
            fileLock = fileChannel.lock();
            return true;
        } catch (Exception e) {
        }
        return false;
    }
