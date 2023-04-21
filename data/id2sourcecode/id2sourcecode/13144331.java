    public boolean grabLock() throws Exception {
        outStream = new FileOutputStream(lockFile);
        try {
            lock = outStream.getChannel().tryLock();
        } catch (OverlappingFileLockException e) {
            return false;
        }
        if (lock != null && lock.isValid()) {
            return true;
        }
        return false;
    }
