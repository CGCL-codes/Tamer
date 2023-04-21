    public boolean connect(SocketAddress addr, int timeout, final ConnectObserver observer) {
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
                NIODispatcher.instance().getScheduledExecutorService().execute(new Runnable() {

                    public void run() {
                        NIODispatcher.instance().register(getChannel(), AbstractNBSocket.this);
                        try {
                            observer.handleConnect(AbstractNBSocket.this);
                        } catch (IOException iox) {
                            NIODispatcher.instance().executeLaterAlways(new Runnable() {

                                public void run() {
                                    shutdown();
                                }
                            });
                        }
                    }
                });
                return true;
            } else {
                NIODispatcher.instance().registerConnect(getChannel(), this, timeout);
                return false;
            }
        } catch (IOException failed) {
            NIODispatcher.instance().executeLaterAlways(new Runnable() {

                public void run() {
                    shutdown();
                }
            });
            return false;
        }
    }
