    private void writeThread() {
        try {
            writeThreadRunner();
        } catch (InterruptedException ex) {
            error.compareAndSet(null, newError("interrupted"));
            interruptControlThread();
        }
    }
