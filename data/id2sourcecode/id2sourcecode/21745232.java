    protected synchronized int available() {
        return writepos - readpos;
    }
