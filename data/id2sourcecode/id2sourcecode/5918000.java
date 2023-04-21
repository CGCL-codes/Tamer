    public boolean acquireWriteLock(long timeout) throws InterruptedException, IllegalStateException {
        LockInfo info = (LockInfo) mLockInfoRef.get();
        int type = info.mType;
        if (type == READ) {
            throw new IllegalStateException("Cannot acquire a write lock while thread holds " + "only a read lock. " + "Use an upgradable lock instead of a read lock.");
        }
        if (type != WRITE) {
            synchronized (this) {
                if (type == UPGRADABLE) {
                    info.mUpgradeCount = info.mCount;
                }
                if (!writeLockAvailable(type)) {
                    if (timeout < 0) {
                        mWriteLockAttempts++;
                        try {
                            while (true) {
                                wait();
                                if (writeLockAvailable(type)) {
                                    break;
                                }
                            }
                        } catch (InterruptedException e) {
                            if (type == UPGRADABLE) {
                                info.mUpgradeCount = 0;
                            }
                            throw e;
                        } finally {
                            mWriteLockAttempts--;
                        }
                    } else if (timeout > 0) {
                        mWriteLockAttempts++;
                        long expire = System.currentTimeMillis() + timeout;
                        try {
                            while (true) {
                                wait(timeout);
                                if (writeLockAvailable(type)) {
                                    break;
                                }
                            }
                            timeout = expire - System.currentTimeMillis();
                            if (timeout <= 0) {
                                return false;
                            }
                        } catch (InterruptedException e) {
                            if (type == UPGRADABLE) {
                                info.mUpgradeCount = 0;
                            }
                            throw e;
                        } finally {
                            mWriteLockAttempts--;
                        }
                    } else {
                        return false;
                    }
                }
                mWriteLockHeld = Thread.currentThread();
            }
            info.mType = WRITE;
        }
        info.mCount++;
        return true;
    }
