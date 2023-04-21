    public synchronized void enterWrite() {
        if (writeLockowner != Thread.currentThread()) {
            while (status != 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            writeLockowner = Thread.currentThread();
        }
        status--;
    }
