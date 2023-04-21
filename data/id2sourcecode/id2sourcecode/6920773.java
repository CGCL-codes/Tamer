    public synchronized void priorityWriteLock() {
        while (activeWriter || activeReaders != 0) {
            try {
                waitingWriters++;
                waitingPriorityWriters++;
                try {
                    wait();
                } finally {
                    waitingWriters--;
                    waitingPriorityWriters--;
                }
            } catch (InterruptedException e) {
                throw new ConcurrentLockException("write lock interrupted in thread");
            }
        }
        activeWriter = true;
    }
