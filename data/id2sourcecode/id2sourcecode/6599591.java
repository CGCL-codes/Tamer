    private Lock acquireLock(ConcurrentAccess configuration) {
        Lock acquiredLock = null;
        if (configuration != null) {
            if (configuration.exclusive()) {
                acquiredLock = this.readWriteLock.writeLock();
            } else {
                acquiredLock = this.readWriteLock.readLock();
            }
            acquiredLock.lock();
            if (this.log.isDebugEnabled()) {
                log.debug("Acquired " + (configuration.exclusive() ? "exclusive " : "") + "lock");
            }
        } else if (this.log.isDebugEnabled()) {
            this.log.debug("No lock acquisition required");
        }
        return acquiredLock;
    }
