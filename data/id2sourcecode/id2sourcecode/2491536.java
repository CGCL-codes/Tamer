    public void releaseSharedLock() {
        this.lock.lock();
        try {
            if (readers == 0 || writers > 0) {
                this.throwError("There should be at least one reader(" + readers + "), and no writers (" + writers + ")");
            }
            readers--;
            if (readers == 0 && writersWaitingCount > 0) {
                writersWaiting.signal();
            } else if (readers == 1 && upgradersWaitingCount > 0) {
                upgradersWaiting.signal();
            } else if (readersWaitingCount > 0 && upgradersWaitingCount == 0 && writersWaitingCount == 0) {
                readersWaiting.signalAll();
            }
        } finally {
            this.lock.unlock();
        }
    }
