    public void closeWriter() throws IOException {
        synchronized (this) {
            writerThread = null;
            writerState = STATE_DEAD;
            deadBuf.removeAllElements();
        }
    }
