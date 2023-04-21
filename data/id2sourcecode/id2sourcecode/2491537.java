    public void releaseExclusiveLock() {
        this.lock.lock();
        try {
            if (readers != 0 || writers != 1) {
                this.throwError("There should be no readers(" + readers + "), and just one writer (" + writers + ")");
            }
            writers--;
            if (writersWaitingCount > 0) {
                writersWaiting.signal();
            } else if (readersWaitingCount > 0) {
                readersWaiting.signalAll();
            }
        } finally {
            this.lock.unlock();
        }
    }
