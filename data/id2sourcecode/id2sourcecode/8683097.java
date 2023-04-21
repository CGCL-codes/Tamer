    public void init() throws IOException {
        if (startPort == 0) {
            port = 0;
            if (log.isInfoEnabled()) log.info("JK: ajp13 disabling channelSocket");
            running = true;
            return;
        }
        if (maxPort < startPort) maxPort = startPort;
        for (int i = startPort; i <= maxPort; i++) {
            try {
                if (inet == null) {
                    sSocket = new ServerSocket(i, backlog);
                } else {
                    sSocket = new ServerSocket(i, backlog, inet);
                }
                port = i;
                break;
            } catch (IOException ex) {
                if (log.isInfoEnabled()) log.info("Port busy " + i + " " + ex.toString());
                continue;
            }
        }
        if (sSocket == null) {
            log.error("Can't find free port " + startPort + " " + maxPort);
            return;
        }
        if (log.isInfoEnabled()) log.info("JK: ajp13 listening on " + getAddress() + ":" + port);
        if ("channelSocket".equals(name) && port != startPort && (wEnv.getLocalId() == 0)) {
            wEnv.setLocalId(port - startPort);
        }
        if (serverTimeout > 0) sSocket.setSoTimeout(serverTimeout);
        if (next == null && wEnv != null) {
            if (nextName != null) setNext(wEnv.getHandler(nextName));
            if (next == null) next = wEnv.getHandler("dispatch");
            if (next == null) next = wEnv.getHandler("request");
        }
        JMXRequestNote = wEnv.getNoteId(WorkerEnv.ENDPOINT_NOTE, "requestNote");
        running = true;
        if (this.domain != null) {
            try {
                tpOName = new ObjectName(domain + ":type=ThreadPool,name=" + getChannelName());
                Registry.getRegistry(null, null).registerComponent(tp, tpOName, null);
                rgOName = new ObjectName(domain + ":type=GlobalRequestProcessor,name=" + getChannelName());
                Registry.getRegistry(null, null).registerComponent(global, rgOName, null);
            } catch (Exception e) {
                log.error("Can't register threadpool");
            }
        }
        tp.start();
        SocketAcceptor acceptAjp = new SocketAcceptor(this);
        tp.runIt(acceptAjp);
    }
