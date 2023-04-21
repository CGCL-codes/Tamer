    public void writeData(String threadID, Object res) {
        rwLock.getWriteLock();
        status = threadID + " has been issued a write lock";
        data.add(new AnytimeResult(threadID, res));
        rwLock.done();
    }
