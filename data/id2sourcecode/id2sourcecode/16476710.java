    public synchronized int available() {
        return writepos - readpos;
    }
