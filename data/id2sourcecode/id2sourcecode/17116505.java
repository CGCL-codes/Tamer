    private void handleRequest(IEnvelope env, RequestTCPRelayMessage message) {
        IPeer peer = message.getTarget();
        IPeerID requestorID = env.getSender().getPeerID();
        SocketChannel sc = natConnector.getControlChannelWith(peer.getPeerID());
        if (sc == null) {
            RelayAnswerMessage ram = new RelayAnswerMessage(myPeerID, requestorID, false, "No control channel to target peer!", null);
            try {
                commFacade.sendTCPMessage((SocketChannel) env.getChannel(), ram);
            } catch (IOException e) {
            }
            return;
        }
        InetSocketAddress addr = new InetSocketAddress(InetAddressUtils.getMostProbableExternalAddress(), NatConnector.DEFAULT_PORT);
        TCPConnRequest request = new TCPConnRequest(myPeerID, peer.getPeerID(), addr);
        BlockingHook bh = BlockingHook.createMessageAnswerHook(request);
        commFacade.getMessageProcessor().installHook(bh, bh.getPredicate());
        IEnvelope answer = null;
        try {
            try {
                commFacade.sendTCPMessage(sc, request);
            } catch (IOException e) {
            }
            answer = bh.waitForMessage();
        } finally {
            commFacade.getMessageProcessor().removeHook(bh);
        }
        if (answer == null) {
            RelayAnswerMessage ram = new RelayAnswerMessage(myPeerID, requestorID, false, "TCPConnRequest not answered by target peer!", null);
            try {
                commFacade.sendTCPMessage((SocketChannel) env.getChannel(), ram);
            } catch (IOException e) {
            }
            return;
        }
        final SocketChannel requestTarget = (SocketChannel) answer.getChannel();
        RelayAnswerMessage relayAnswer = new RelayAnswerMessage(myPeerID, env.getSender().getPeerID(), true, "", null);
        try {
            commFacade.sendTCPMessage((SocketChannel) env.getChannel(), relayAnswer);
        } catch (IOException e) {
            Logger.logError(e, "Could not send (positive) relay answer!");
        }
        final SocketChannel requestor = (SocketChannel) env.getChannel();
        daemon.addMapping(requestor, requestTarget);
    }
