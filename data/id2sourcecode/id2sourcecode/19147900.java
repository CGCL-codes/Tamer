    public synchronized long readPendingCount() {
        return readCount - writeCount;
    }
