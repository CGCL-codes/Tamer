    public void update() throws IOException {
        if (!processLock.isLocked()) throw new IllegalStateException("Process lock has not been obtained"); else if (worker == null && isOfflineLockEnabled() == false) doSyncDown(); else throw new IllegalStateException("update should not be called in " + "offline or read-write mode.");
    }
