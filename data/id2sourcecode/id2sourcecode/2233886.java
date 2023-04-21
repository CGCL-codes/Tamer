    public synchronized void exitWrite() {
        if (writeLockowner != Thread.currentThread()) throw new IllegalStateException("Current owner is " + writeLockowner);
        if (++status == 0) {
            writeLockowner = null;
            notifyAll();
        }
    }
