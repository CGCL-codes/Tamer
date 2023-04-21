    public synchronized void writeLock() {
        while (activeWriter || activeReaders != 0 || waitingPriorityWriters != 0) {
            try {
                waitingWriters++;
                try {
                    wait();
                } finally {
                    waitingWriters--;
                }
            } catch (InterruptedException e) {
                throw new ConcurrentLockException("write lock interrupted in thread");
            }
        }
        activeWriter = true;
    }
