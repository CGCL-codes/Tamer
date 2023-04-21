    private void ensureLockHeld() {
        if (!m_testLockIsHeld) return;
        if (m_readWriteLock == null) return;
        if (!LockUtil.isLockHeld(m_readWriteLock.readLock()) && !LockUtil.isLockHeld(m_readWriteLock.writeLock())) {
            new Exception("Lock not held").printStackTrace(System.out);
        }
    }
