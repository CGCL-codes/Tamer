    @Override
    protected IConnectionInfo<SocketChannel> establishConnImpl(IPeer remotePeer, ConnectionRequestType type) {
        IPeerID myID = getConnBroker().getCallback().getOwnPeerID();
        InetSocketAddress natSSCAddress = new InetSocketAddress(InetAddressUtils.getMostProbableExternalAddress(), NatConnector.DEFAULT_PORT);
        IMessage msg = new TCPConnRequest(myID, remotePeer.getPeerID(), natSSCAddress);
        SocketChannel controlChannel = getConnBroker().getControlChannelManager().getControlChannelWith(remotePeer.getPeerID());
        if (controlChannel == null) {
            msg = new RelayMessage(myID, null, remotePeer.getPeerID(), msg);
            controlChannel = getConnBroker().getControlChannelManager().getControlChannelFor(remotePeer.getPeerID());
        }
        if (controlChannel == null) {
            Logger.logWarning("[TCPConnReversal] Neither got a direct nor a indirect control channel to the peer " + remotePeer + ", mechanism failed!");
            return null;
        }
        BlockingHook bh = BlockingHook.createMessageAnswerHook(msg);
        IMessageProcessor msgProc = getConnBroker().getCommFacade().getMessageProcessor();
        msgProc.installHook(bh, bh.getPredicate());
        IEnvelope answer = null;
        try {
            try {
                getConnBroker().getCommFacade().sendTCPMessage(controlChannel, msg);
            } catch (IOException e) {
                Logger.logError(e, "Error sending relay message with TCP conn request.");
            }
            answer = bh.waitForMessage();
        } finally {
            msgProc.removeHook(bh);
        }
        if (answer != null) {
            SocketChannel sc = (SocketChannel) answer.getChannel();
            System.out.println("[TCPConnReversal] Successfully established TCP connection to " + remotePeer);
            return new ConnectionInfo<SocketChannel>(sc, false, false, null, "ConnectionReversal");
        } else return null;
    }
