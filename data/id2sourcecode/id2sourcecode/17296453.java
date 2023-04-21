    public void closeReader() throws IOException {
        synchronized (this) {
            writerThread = null;
            writerState = STATE_DEAD;
            activeBuf.removeAllElements();
        }
    }
