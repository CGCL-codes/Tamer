    private String status() {
        return " read locks:" + rwlock.getReadLockCount() + " writeLocks:" + rwlock.getWriteHoldCount() + " waiting:" + rwlock.getQueueLength() + "\n";
    }
