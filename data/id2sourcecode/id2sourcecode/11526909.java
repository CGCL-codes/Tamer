    public boolean isWritable() {
        return (!readOnly && writeLock.isLocked());
    }
