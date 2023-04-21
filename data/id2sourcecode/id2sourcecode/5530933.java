    public synchronized Object getReadLock() {
        while (readersWaiting > 0 || writersWaiting > 0 || writeLockHeld) {
            try {
                readersWaiting++;
                wait();
                readersWaiting--;
                if (readersWaiting > 0 && writersWaiting == 0) {
                    notify();
                }
                break;
            } catch (InterruptedException e) {
            }
        }
        numReaders++;
        printErrorStatus("getReadLock:");
        return readWriteObject;
    }
