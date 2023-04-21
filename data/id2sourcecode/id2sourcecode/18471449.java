    private long write0(ByteBuffer[] bufs) throws IOException {
        if (bufs == null) throw new NullPointerException();
        synchronized (writeLock) {
            synchronized (stateLock) {
                ensureOpen();
                if (!isConnected()) throw new NotYetConnectedException();
            }
            long n = 0;
            try {
                begin();
                if (!isOpen()) return 0;
                writerThread = NativeThread.current();
                do {
                    n = IOUtil.write(fd, bufs, nd);
                } while ((n == IOStatus.INTERRUPTED) && isOpen());
                return IOStatus.normalize(n);
            } finally {
                writerThread = 0;
                end((n > 0) || (n == IOStatus.UNAVAILABLE));
                assert IOStatus.check(n);
            }
        }
    }
