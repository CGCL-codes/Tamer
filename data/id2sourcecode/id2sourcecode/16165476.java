    public AEProxyImpl(int _port, long _connect_timeout, long _read_timeout, AEProxyHandler _proxy_handler) throws AEProxyException {
        port = _port;
        connect_timeout = _connect_timeout;
        read_timeout = _read_timeout;
        proxy_handler = _proxy_handler;
        String name = "Proxy:" + port;
        read_selector = new VirtualChannelSelector(name, VirtualChannelSelector.OP_READ, false);
        connect_selector = new VirtualChannelSelector(name, VirtualChannelSelector.OP_CONNECT, true);
        write_selector = new VirtualChannelSelector(name, VirtualChannelSelector.OP_WRITE, true);
        try {
            final ServerSocketChannel ssc = ServerSocketChannel.open();
            ServerSocket ss = ssc.socket();
            ss.setReuseAddress(true);
            ss.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port), 128);
            if (port == 0) {
                port = ss.getLocalPort();
            }
            Thread connect_thread = new AEThread("AEProxy:connect.loop") {

                public void runSupport() {
                    selectLoop(connect_selector);
                }
            };
            connect_thread.setDaemon(true);
            connect_thread.start();
            Thread read_thread = new AEThread("AEProxy:read.loop") {

                public void runSupport() {
                    selectLoop(read_selector);
                }
            };
            read_thread.setDaemon(true);
            read_thread.start();
            Thread write_thread = new AEThread("AEProxy:write.loop") {

                public void runSupport() {
                    selectLoop(write_selector);
                }
            };
            write_thread.setDaemon(true);
            write_thread.start();
            Thread accept_thread = new AEThread("AEProxy:accept.loop") {

                public void runSupport() {
                    acceptLoop(ssc);
                }
            };
            accept_thread.setDaemon(true);
            accept_thread.start();
            if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, "AEProxy: listener established on port " + port));
        } catch (Throwable e) {
            Logger.logTextResource(new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_ERROR, "Tracker.alert.listenfail"), new String[] { "" + port });
            if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, "AEProxy: listener failed on port " + port, e));
            throw (new AEProxyException("AEProxy: accept fails: " + e.toString()));
        }
    }
