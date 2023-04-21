    public synchronized void lock(String[] read, String[] write) throws RequestException {
        if (isLocked()) throw new RequestException("failed to lock because we already own a lock", Reason.LOCKED);
        prepareLock();
        worker.lock(read, write);
    }
