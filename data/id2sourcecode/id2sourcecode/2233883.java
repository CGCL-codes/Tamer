    public synchronized void enterRead() {
        if (writeLockowner == Thread.currentThread()) return;
        while (status < 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        status++;
    }
