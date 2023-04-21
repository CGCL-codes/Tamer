    public int send(ByteBuffer src, SocketAddress target) throws IOException {
        if (src == null) throw new NullPointerException();
        synchronized (writeLock) {
            ensureOpen();
            InetSocketAddress isa = (InetSocketAddress) target;
            InetAddress ia = isa.getAddress();
            if (ia == null) throw new IOException("Target address not resolved");
            synchronized (stateLock) {
                if (!isConnected()) {
                    if (target == null) throw new NullPointerException();
                    SecurityManager sm = System.getSecurityManager();
                    if (sm != null) {
                        if (ia.isMulticastAddress()) {
                            sm.checkMulticast(isa.getAddress());
                        } else {
                            sm.checkConnect(isa.getAddress().getHostAddress(), isa.getPort());
                        }
                    }
                } else {
                    if (!target.equals(remoteAddress)) {
                        throw new IllegalArgumentException("Connected address not equal to target address");
                    }
                    return write(src);
                }
            }
            int n = 0;
            try {
                begin();
                if (!isOpen()) return 0;
                writerThread = NativeThread.current();
                do {
                    n = send(fd, src, target);
                } while ((n == IOStatus.INTERRUPTED) && isOpen());
                synchronized (stateLock) {
                    if (isOpen() && (localAddress == null)) {
                        localAddress = Net.localAddress(fd);
                    }
                }
                return IOStatus.normalize(n);
            } finally {
                writerThread = 0;
                end((n > 0) || (n == IOStatus.UNAVAILABLE));
                assert IOStatus.check(n);
            }
        }
    }
