    public synchronized Object getWriteLock() {
        while (readersWaiting > 0 || writersWaiting > 0 || writeLockHeld || numReaders > 0) {
            try {
                writersWaiting++;
                wait();
                if (numReaders == 0) {
                    writersWaiting--;
                    break;
                }
            } catch (InterruptedException e) {
            }
        }
        writeLockHeld = true;
        printErrorStatus("getWriteLock:");
        return readWriteObject;
    }
