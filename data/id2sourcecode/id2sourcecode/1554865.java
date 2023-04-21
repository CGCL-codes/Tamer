    public DatagramChannel requestRelayedUDPChannel(IPeer targetPeer, InetSocketAddress relayPeerAddr, InetSocketAddress bindTo) {
        Logger.log("Trying to relay to " + targetPeer + " over " + relayPeerAddr);
        InetSocketAddress useAddress = null;
        ICommFacade commFacade = getCommFacade();
        IPeerID myPeerID = getCallback().getOwnPeerID();
        final SocketChannel controlConnection = getControlChannelManager().getControlChannelWith(relayPeerAddr.getAddress());
        if (controlConnection == null) return null;
        BlockingHook bh = BlockingHook.createAwaitMessageHook(controlConnection, RelayAnswerMessage.class);
        commFacade.getMessageProcessor().installHook(bh, bh.getPredicate());
        RelayAnswerMessage answer = null;
        try {
            try {
                commFacade.sendTCPMessage(controlConnection, new RequestUDPRelayMessage(myPeerID, null, targetPeer));
            } catch (IOException e) {
            }
            IEnvelope env = bh.waitForMessage();
            answer = (env != null ? (RelayAnswerMessage) env.getMessage() : null);
        } finally {
            commFacade.getMessageProcessor().removeHook(bh);
        }
        if (answer == null) {
            Logger.logWarning("Did not receive answer of RequestUDPRelayMessage, returning null");
            return null;
        }
        if (answer.isRelayEstablished()) {
            useAddress = answer.getUseAddress();
            Logger.log("Received RelayAnswerMessage: " + answer);
        } else {
            Logger.logWarning("Relay could not be established: " + answer.getReason());
        }
        closeWithoutException(controlConnection);
        if (useAddress != null) {
            try {
                DatagramChannel result = DatagramChannel.open();
                result.socket().bind(null);
                result.configureBlocking(false);
                commFacade.getChannelManager().registerChannel(result);
                commFacade.sendUDPMessage(result, new RelayAnswerACK(myPeerID, answer.getSenderPeerID()), useAddress);
                waitForComplete(result);
                if (!result.isConnected()) {
                    closeWithoutException(result);
                    return null;
                }
                return result;
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }
