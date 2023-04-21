    @Override
    protected Lock lockWrite() {
        Lock lock = m_readWriteLock.writeLock();
        lock.lock();
        return lock;
    }
