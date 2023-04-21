    public void releaseWriteLock() {
        if (m_readWriteLock == null) return;
        LockUtil.releaseLock(m_readWriteLock.writeLock());
    }
