    public void acquireWriteLock() {
        if (m_readWriteLock == null) return;
        LockUtil.acquireLock(m_readWriteLock.writeLock());
    }
