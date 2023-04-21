    private void invokeRun0(final LockProtectedRunnable task, boolean sync, Lock lock) {
        final boolean write = lock.isWriteLock();
        class Helper implements Runnable, LockProtectedRunnable {

            private boolean executed;

            private Lock retainedLock;

            public void run() {
                synchronized (this) {
                    execute(this, write);
                    if (executed) {
                        return;
                    }
                    while (retainedLock == null) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    executeForcedly(task, retainedLock);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            public void run(boolean sync, Lock lo) {
                if (isAllowedThread(lo.isWriteLock())) {
                    executed = true;
                    invokeRun(task, false, lo);
                } else {
                    lo.retain();
                    retainedLock = lo;
                    synchronized (this) {
                        notifyAll();
                    }
                }
            }
        }
        if (task instanceof Helper) {
            task.run(sync, lock);
        } else if (isAllowedThread(write)) {
            invokeRun(task, sync, lock);
        } else {
            executeInAllowedThread(new Helper());
        }
    }
