    public int write(ByteBuffer buf) throws IOException {
        if (buf == null) throw new NullPointerException();
        synchronized (writeLock) {
            ensureWriteOpen();
            int n = 0;
            try {
                begin();
                synchronized (stateLock) {
                    if (!isOpen()) return 0;
                    writerThread = NativeThread.current();
                }
                for (; ; ) {
                    n = IOUtil.write(fd, buf, -1, nd, writeLock);
                    if ((n == IOStatus.INTERRUPTED) && isOpen()) continue;
                    return IOStatus.normalize(n);
                }
            } finally {
                writerCleanup();
                end(n > 0 || (n == IOStatus.UNAVAILABLE));
                synchronized (stateLock) {
                    if ((n <= 0) && (!isOutputOpen)) throw new AsynchronousCloseException();
                }
                assert IOStatus.check(n);
            }
        }
    }
