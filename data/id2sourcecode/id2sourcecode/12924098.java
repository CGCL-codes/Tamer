    public synchronized int tryAcquire(Transaction ta, int newLevel) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest(this + " ta.getID(): " + ta.taID() + " newLevel: " + newLevel);
        }
        if (newLevel <= LEVEL_NONE || newLevel >= LEVEL_MAX) {
            throw new IllegalArgumentException("invalid lock level value");
        }
        int result;
        if (readLock.isAcquiredBy(ta) && level >= newLevel) {
            result = level;
        } else if (level == LEVEL_NONE) {
            result = LEVEL_NONE;
            if (readLock.tryAcquire(ta, LEVEL_READ) == NOT_ACQUIRED) {
                throw new OzoneInternalException("cannot get lock on shared read lock");
            }
            if (newLevel > LEVEL_READ && writeLock.tryAcquire(ta, LEVEL_WRITE) == NOT_ACQUIRED) {
                throw new OzoneInternalException("cannot get lock on exclusive write lock that should be empty");
            }
        } else if (readLock.isAcquiredBy(ta)) {
            result = (readLock.areMultipleLockersHoldingLocks() || writeLock.tryAcquire(ta, LEVEL_WRITE) == NOT_ACQUIRED) ? NOT_ACQUIRED : level;
        } else if (newLevel == LEVEL_READ && level == LEVEL_READ) {
            result = LEVEL_NONE;
            if (readLock.tryAcquire(ta, LEVEL_READ) == NOT_ACQUIRED) {
                throw new OzoneInternalException("cannot get lock on shared read lock");
            }
        } else {
            result = NOT_ACQUIRED;
        }
        if (result != NOT_ACQUIRED && newLevel > level) {
            level = newLevel;
        }
        return result;
    }
