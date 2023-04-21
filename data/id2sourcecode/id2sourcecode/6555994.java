    @Override
    public void sessionCreated(IoSession session) throws Exception {
        boolean isClient = session.getRemoteAddress().equals(forward);
        if (log.isDebugEnabled()) {
            log.debug("Is downstream: " + isClient);
            session.setAttribute(ProtocolState.SESSION_KEY, new RTMP(isClient));
            session.getFilterChain().addFirst("protocol", new ProtocolCodecFilter(codecFactory));
        }
        session.getFilterChain().addFirst("proxy", new ProxyFilter(isClient ? "client" : "server"));
        if (true) {
            String fileName = System.currentTimeMillis() + "_" + forward.getHostName() + "_" + forward.getPort() + "_" + (isClient ? "DOWNSTREAM" : "UPSTREAM");
            File headersFile = loader.getResource(dumpTo + fileName + ".cap").getFile();
            headersFile.createNewFile();
            File rawFile = loader.getResource(dumpTo + fileName + ".raw").getFile();
            rawFile.createNewFile();
            FileOutputStream headersFos = new FileOutputStream(headersFile);
            WritableByteChannel headers = headersFos.getChannel();
            FileOutputStream rawFos = new FileOutputStream(rawFile);
            WritableByteChannel raw = rawFos.getChannel();
            ByteBuffer header = ByteBuffer.allocate(1);
            header.put((byte) (isClient ? 0x00 : 0x01));
            header.flip();
            headers.write(header.buf());
            session.getFilterChain().addFirst("dump", new NetworkDumpFilter(headers, raw));
        }
        if (!isClient) {
            log.debug("Connecting..");
            SocketConnector connector = new SocketConnector();
            ConnectFuture future = connector.connect(forward, this);
            future.join();
            if (future.isConnected()) {
                log.debug("Connected: " + forward);
                IoSession client = future.getSession();
                client.setAttribute(ProxyFilter.FORWARD_KEY, session);
                session.setAttribute(ProxyFilter.FORWARD_KEY, client);
            }
        }
        super.sessionCreated(session);
    }
