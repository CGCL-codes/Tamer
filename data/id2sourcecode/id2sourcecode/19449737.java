    public void writeData(String threadID, Object res) {
        rwLock.getWriteLock();
        status = threadID + " has been issued a write lock";
        System.out.println(status);
        System.out.println("Thread " + threadID + " is writing data");
        data.add(new AnytimeResult(threadID, res));
        rwLock.done();
    }
