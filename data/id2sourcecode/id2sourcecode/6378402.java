    public void writeLock() {
        if (this.lock.getReadHoldCount() > 0) {
            throw new DeadLockException("The application is attempting to write lock during a read only operation and will deadlock.\n" + "This problem can occur when trying to write from a read only query. Use unguarded or update queries instead. ");
        }
        this.lock.writeLock().lock();
    }
