    public int write(ByteBuffer buf) throws IOException {
        if (buf == null) throw new NullPointerException();
        synchronized (writeLock) {
            synchronized (stateLock) {
                ensureOpen();
                if (!isConnected()) throw new NotYetConnectedException();
            }
            int n = 0;
            try {
                begin();
                if (!isOpen()) return 0;
                writerThread = NativeThread.current();
                do {
                    n = IOUtil.write(fd, buf, -1, nd, writeLock);
                } while ((n == IOStatus.INTERRUPTED) && isOpen());
                return IOStatus.normalize(n);
            } finally {
                writerThread = 0;
                end((n > 0) || (n == IOStatus.UNAVAILABLE));
                assert IOStatus.check(n);
            }
        }
    }
