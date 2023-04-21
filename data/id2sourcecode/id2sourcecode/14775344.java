    private final boolean allowRead() {
        return (Thread.currentThread() == writerThread) || (waitingWriters == 0 && activeWriters == 0);
    }
