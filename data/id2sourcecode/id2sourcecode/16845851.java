    public void sendFile(IPeer peer, File file) {
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open(peer.getAddress());
        } catch (IOException e1) {
            Logger.logWarning("Could not establish direct TCP connection to " + peer + ", trying to get one from the broker..");
        }
        if (sc == null || !sc.isOpen() || !sc.isConnected()) {
            sc = chatModel.getConnectionBroker().requestTCPChannel(peer);
        }
        if (sc == null) {
            Logger.log("Could not establish TCP connection to peer: " + peer);
            return;
        }
        try {
            if (sc != null && sc.isBlocking()) sc.configureBlocking(false);
        } catch (IOException e1) {
        }
        commFacade.getChannelManager().registerChannel(sc);
        sendFile(file, sc);
    }
