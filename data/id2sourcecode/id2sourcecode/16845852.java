    public void sendFile(IPeer peer, File file, InetSocketAddress relayAddress) {
        SocketChannel sc = chatModel.getConnectionBroker().requestRelayedTCPChannel(peer, relayAddress);
        if (sc == null) {
            Logger.log("Could not establish relayed TCP connection to peer: " + peer);
            return;
        }
        try {
            sc.configureBlocking(false);
        } catch (IOException e) {
        }
        commFacade.getChannelManager().registerChannel(sc);
        sendFile(file, sc);
    }
