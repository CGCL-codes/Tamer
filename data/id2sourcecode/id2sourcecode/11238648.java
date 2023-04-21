    public boolean hasLock(Thread owner) {
        if (writeLockedThread == owner) return true;
        return hasReadLock(owner);
    }
