    public boolean numPending() {
        return writePos - readPos != 0;
    }
