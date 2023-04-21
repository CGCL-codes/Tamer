    private boolean registerControlChannel(SocketChannel sc, boolean deregisterSelectors) {
        commFacade.getChannelManager().registerChannel(sc);
        sendIntroduction(sc);
        Logger.log("Established TCP connection for NAT-traversal to " + sc.socket().getRemoteSocketAddress());
        return true;
    }
