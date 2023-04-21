    public synchronized boolean lockForUpdates() {
        if (_lock != null && _lock.isValid()) {
            return true;
        }
        try {
            _lockChannel = new RandomAccessFile(getLocalPath("gettingdown.lock"), "rw").getChannel();
        } catch (FileNotFoundException e) {
            log.warning("Unable to create lock file", "message", e.getMessage(), e);
            return false;
        }
        try {
            _lock = _lockChannel.tryLock();
        } catch (IOException e) {
            log.warning("Unable to create lock", "message", e.getMessage(), e);
            return false;
        } catch (OverlappingFileLockException e) {
            log.warning("The lock is held elsewhere in this JVM", e);
            return false;
        }
        log.info("Able to lock for updates: " + (_lock != null));
        return _lock != null;
    }
