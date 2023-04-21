    public boolean connect(SocketAddress addr, int timeout, ConnectObserver observer) {
        synchronized (LOCK) {
            if (shutdown) {
                observer.shutdown();
                return false;
            }
            this.connecter = observer;
        }
        try {
            InetSocketAddress iaddr = (InetSocketAddress) addr;
            if (iaddr.isUnresolved()) throw new IOException("unresolved: " + addr);
            if (getChannel().connect(addr)) {
                observer.handleConnect(this);
                return true;
            } else {
                NIODispatcher.instance().registerConnect(getChannel(), this, timeout);
                return false;
            }
        } catch (IOException failed) {
            NIODispatcher.instance().invokeReallyLater(new Runnable() {

                public void run() {
                    shutdown();
                }
            });
            return false;
        }
    }
