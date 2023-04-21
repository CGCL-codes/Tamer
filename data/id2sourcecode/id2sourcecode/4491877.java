    @Override
    public void beginWrite() {
        readWriteLock.writeLock().lock();
    }
