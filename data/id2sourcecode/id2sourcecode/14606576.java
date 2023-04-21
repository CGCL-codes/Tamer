    public boolean testUDPConnectivity(DatagramChannel datagramChannel, InetSocketAddress addr) {
        if (datagramChannel.isConnected() && addr != null && !datagramChannel.socket().getRemoteSocketAddress().equals(addr)) throw new IllegalArgumentException("When socket is connected, address as to be null or the remote address!");
        if (!datagramChannel.isConnected() && addr == null) throw new IllegalArgumentException("Address must not be null for unconnected channels!");
        if (datagramChannel.isConnected()) addr = ConnectionTargetRequestor.getInstance().getRemoteAddress(datagramChannel);
        boolean result = false;
        boolean hasBeenRegistered = commFacade.getChannelManager().getSelectionKey(datagramChannel) != null;
        if (!hasBeenRegistered && commFacade.getChannelManager().registerChannel(datagramChannel) == null) {
            Logger.logError(null, "Could not register channel to test connectivity!");
            return false;
        }
        try {
            IMessageProcessor msgProc = commFacade.getMessageProcessor();
            BlockingHook bh = BlockingHook.createAwaitMessageHook(datagramChannel, UDPPong.class);
            msgProc.installHook(bh, bh.getPredicate());
            try {
                Logger.log("Testing this socket: " + SocketFormatter.formatChannel(datagramChannel));
                IMessage msg = new UDPPing(getOwnPeerID());
                commFacade.sendUDPMessage(datagramChannel, msg, addr);
                IEnvelope env = bh.waitForMessage();
                result = (env != null);
            } finally {
                msgProc.removeHook(bh);
            }
        } catch (Exception e) {
            result = false;
        }
        if (!hasBeenRegistered) commFacade.getChannelManager().unregisterChannel(datagramChannel);
        return result;
    }
