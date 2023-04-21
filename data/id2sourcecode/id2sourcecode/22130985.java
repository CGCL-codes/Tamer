    public void enable(boolean read, boolean write) {
        readLogger.setLevel(read ? Level.DEBUG : Level.WARN);
        writeLogger.setLevel(write ? Level.DEBUG : Level.WARN);
    }
