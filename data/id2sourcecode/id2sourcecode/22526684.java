    public void executeForcedly(LockProtectedRunnable task, Lock retained) throws InterruptedException {
        boolean write = ((LockImpl) retained).write;
        checkThread(write);
        task.getClass();
        LockImpl lock = null;
        synchronized (mutex) {
            while ((lock = lock(true, write)) == null) {
                mutex.wait();
            }
            ((LockImpl) retained).use();
        }
        executeImpl(task, lock);
    }
