    public long write0(ByteBuffer[] bufs) throws IOException {
        if (bufs == null) throw new NullPointerException();
        synchronized (writeLock) {
            ensureWriteOpen();
            long n = 0;
            try {
                begin();
                synchronized (stateLock) {
                    if (!isOpen()) return 0;
                    writerThread = NativeThread.current();
                }
                for (; ; ) {
                    n = IOUtil.write(fd, bufs, nd);
                    if ((n == IOStatus.INTERRUPTED) && isOpen()) continue;
                    return IOStatus.normalize(n);
                }
            } finally {
                writerCleanup();
                end((n > 0) || (n == IOStatus.UNAVAILABLE));
                synchronized (stateLock) {
                    if ((n <= 0) && (!isOutputOpen)) throw new AsynchronousCloseException();
                }
                assert IOStatus.check(n);
            }
        }
    }
