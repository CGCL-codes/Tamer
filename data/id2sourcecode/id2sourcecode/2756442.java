    synchronized void closeWriter() {
        if (writerClosed) throw new IllegalStateException("Already closed");
        writerClosed = true;
        notifyAll();
    }
