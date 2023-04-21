    public void acquireWriterLock() throws InterruptedException {
        WaitObject wo = null;
        synchronized (this) {
            if (this.writerLockOwner == Thread.currentThread()) return;
            this.numberOfWaitingWriters++;
            if ((this.writerLockOwner != null) || (numberOfActiveReaders > 0)) {
                wo = new WaitObject();
                wo.isWriter = true;
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
                this.writerLockOwner = Thread.currentThread();
                this.numberOfWaitingWriters--;
            }
        }
    }
