    protected void checkState(boolean openForWrite) throws IllegalArgumentException {
        if (this.openForWrite != openForWrite) {
            cleanup();
            if (openForWrite) throw new IllegalArgumentException("Can't write message, this factory is reading."); else throw new IllegalArgumentException("Can't read message, this factory is writing.");
        }
        if (this.closed) {
            cleanup();
            throw new IllegalArgumentException("Factory has been closed.");
        }
    }
