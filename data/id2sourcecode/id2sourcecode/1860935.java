    public boolean blockDelegateExecution(final int timeToWaitMS) throws InterruptedException {
        final boolean rVal = m_readWriteLock.writeLock().tryLock(timeToWaitMS, TimeUnit.MILLISECONDS);
        if (!rVal) {
        } else {
            if (sm_logger.isLoggable(Level.FINE)) sm_logger.fine(Thread.currentThread().getName() + " block delegate execution.");
        }
        return rVal;
    }
