    protected void markStateCorrupted() {
        Preconditions.checkState(writeLock.isHeldByCurrentThread(), "must hold write lock");
        state = State.CORRUPTED;
    }
