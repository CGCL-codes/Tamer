    @Override
    public String toString() {
        int state = mState;
        int readLocks = state & ~LOCK_STATE_MASK;
        int upgradeLocks = mUpgradeCount;
        int writeLocks = mWriteCount;
        return super.toString() + "[Read locks = " + readLocks + ", Upgrade locks = " + upgradeLocks + ", Write locks = " + writeLocks + ", Owner = " + mOwner + ']';
    }
