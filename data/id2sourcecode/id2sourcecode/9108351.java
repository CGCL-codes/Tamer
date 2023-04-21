    public synchronized void lockRead() throws InterruptedException {
        if (activeReaders != 0 || allowRead()) {
            ++activeReaders;
            return;
        }
        if (Thread.currentThread() == writerThread) Debug.bug();
        ++waitingReaders;
        while (!allowRead()) {
            try {
                wait();
            } catch (InterruptedException e) {
                --waitingReaders;
                throw e;
            }
        }
        --waitingReaders;
        ++activeReaders;
    }
