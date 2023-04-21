    protected boolean doIsConnected(String name) {
        SocketChannel sc = null;
        Initiator initiator = container.getInitiator(name);
        sc = (SocketChannel) initiator.getChannel();
        if (sc == null) {
            return false;
        } else {
            return sc.socket().isConnected();
        }
    }
