    protected void internalInitialize() throws Exception {
        port = serverSocket.getLocalPort();
        orb.getCorbaTransportManager().getInboundConnectionCache(this);
        serverSocketChannel = serverSocket.getChannel();
        if (serverSocketChannel != null) {
            setUseSelectThreadToWait(orb.getORBData().acceptorSocketUseSelectThreadToWait());
            serverSocketChannel.configureBlocking(!orb.getORBData().acceptorSocketUseSelectThreadToWait());
        } else {
            setUseSelectThreadToWait(false);
        }
        setUseWorkerThreadForEvent(orb.getORBData().acceptorSocketUseWorkerThreadForEvent());
    }
