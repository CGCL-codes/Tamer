    public boolean isClosed() {
        return readPool.isClosed() || writePool.isClosed();
    }
