    public void writeData(int threadID, Result res) {
        rwLock.getWriteLock();
        status = threadID + " has been issued a write lock";
        System.out.println(status);
        System.out.println("Thread " + threadID + " is writing data");
        data.add(res);
        rwLock.done();
    }
