    public void downgradeToSharedLock() {
        this.lock.lock();
        try {
            if (readers != 0 || writers != 1) {
                this.throwError("There should be no readers(" + readers + "), and just one writer (" + writers + ")");
            }
            writers--;
            readers++;
            if (readersWaitingCount > 0) {
                readersWaiting.signalAll();
            }
        } finally {
            this.lock.unlock();
        }
    }
