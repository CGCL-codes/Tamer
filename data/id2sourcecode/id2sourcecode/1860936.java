    public void resumeDelegateExecution() {
        if (sm_logger.isLoggable(Level.FINE)) sm_logger.fine(Thread.currentThread().getName() + " resumes delegate execution.");
        m_readWriteLock.writeLock().unlock();
    }
