    public void initWriter() {
        synchronized (this) {
            activeBuf.removeAllElements();
            writerThread = Thread.currentThread();
            writerState = STATE_ACTIVE;
            framesWritten = 0;
        }
    }
