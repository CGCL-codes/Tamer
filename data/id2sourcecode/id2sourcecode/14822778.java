    public synchronized void unsetComponent(ITestComponent c) {
        readWriteLock.writeLock().lock();
        component = null;
        readWriteLock.writeLock().unlock();
    }
