    public void executeForcedly(LockProtectedRunnable task, boolean write) throws InterruptedException {
        checkThread(write);
        task.getClass();
        LockImpl lock = null;
        synchronized (mutex) {
            while ((lock = lock(false, write)) == null) {
                mutex.wait();
            }
        }
        executeImpl(task, lock);
    }
