    public synchronized long writePendingCount() {
        return writeCount - readCount;
    }
