    public void acquireReaderLock() throws InterruptedException {
        WaitObject wo = null;
        synchronized (this) {
            if (this.writerLockOwner == Thread.currentThread()) {
                numberOfActiveReaders++;
                return;
            }
            if ((this.numberOfWaitingWriters > 0) || (this.writerLockOwner != null)) {
                wo = new WaitObject();
                this.addToQueue(wo);
            }
        }
        try {
            if (wo != null) {
                synchronized (wo) {
                    while (!wo.isItMyTurn) wo.wait();
                }
            }
        } finally {
            synchronized (this) {
                if (wo != null) {
                    this.wakeUpNextInQueue(false);
                }
                numberOfActiveReaders++;
            }
        }
    }
