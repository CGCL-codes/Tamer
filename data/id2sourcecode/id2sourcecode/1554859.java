    public SocketChannel requestRelayedTCPChannel(IPeer to, InetSocketAddress relayHost) {
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open(relayHost);
            sc.configureBlocking(false);
            commFacade.getChannelManager().registerChannel(sc);
            RequestTCPRelayMessage msg = new RequestTCPRelayMessage(getCallback().getOwnPeerID(), null, to);
            BlockingHook bh = BlockingHook.createAwaitMessageHook(sc, RelayAnswerMessage.class);
            commFacade.getMessageProcessor().installHook(bh, bh.getPredicate());
            IEnvelope env = null;
            try {
                commFacade.sendTCPMessage(sc, msg);
                env = bh.waitForMessage();
            } finally {
                commFacade.getMessageProcessor().removeHook(bh);
            }
            if (env == null) {
                Logger.logError(null, "Received no answer of RequestTCPRelayMessage, TCP relay wont work!");
                return null;
            }
            RelayAnswerMessage answer = (RelayAnswerMessage) env.getMessage();
            if (answer.isRelayEstablished()) return sc; else return null;
        } catch (IOException e) {
            if (sc != null) {
                closeWithoutException(sc);
            }
            return null;
        }
    }
