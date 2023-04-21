    public SocketChannel getControlChannelWith(InetAddress remoteAddr) {
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.socket().bind(null);
            sc.connect(new InetSocketAddress(remoteAddr, NatConnector.DEFAULT_PORT));
            sc.configureBlocking(false);
            connBroker.getCommFacade().getChannelManager().registerChannel(sc);
            return sc;
        } catch (IOException e) {
            closeWithoutException(sc);
            return null;
        }
    }
