    protected static void timeoutTest() {
        readerThread();
        writerThread();
        writerThread2();
        readerThread();
    }
