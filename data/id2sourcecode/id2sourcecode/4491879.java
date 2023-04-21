    @Override
    public void endWrite() {
        readWriteLock.writeLock().unlock();
    }
