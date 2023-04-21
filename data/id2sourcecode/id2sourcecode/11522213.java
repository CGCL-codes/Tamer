    public void aquireWriteLock() {
        readWriteLock.writeLock().lock();
    }
