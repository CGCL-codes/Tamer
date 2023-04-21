    private void handleRequest(IEnvelope env, RequestUDPRelayMessage message) {
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
        final DatagramChannel requestor = openLocalUDPChannel();
        final DatagramChannel relayTarget = openLocalUDPChannel();
        final UDPPunchRequest udpPunchMsg = new UDPPunchRequest(myPeerID, requestorID, peer.getAddress().getPort(), false, new InetSocketAddress(InetAddressUtils.getMostProbableExternalAddress(), relayTarget.socket().getLocalPort()));
        BlockingHook waitForPunchAnswer = new BlockingHook(new IPredicate<IEnvelope>() {

            public boolean appliesTo(IEnvelope obj) {
                return (obj.getMessage() instanceof UDPPunchAnswer && ((UDPPunchAnswer) obj.getMessage()).getPunchMessageID().equals(udpPunchMsg.getMessageID()));
            }
        });
        BlockingHook waitForUDPPing = BlockingHook.createAwaitMessageHook(relayTarget, UDPPing.class);
        IEnvelope ping = null;
        UDPPunchAnswer answer = null;
        IEnvelope punchAnswerEnvelope = null;
        commFacade.getMessageProcessor().installHook(waitForPunchAnswer, waitForPunchAnswer.getPredicate());
        commFacade.getMessageProcessor().installHook(waitForUDPPing, waitForUDPPing.getPredicate());
        try {
            try {
                commFacade.sendTCPMessage(sc, udpPunchMsg);
            } catch (IOException e) {
            }
            punchAnswerEnvelope = waitForPunchAnswer.waitForMessage();
            answer = (punchAnswerEnvelope != null ? (UDPPunchAnswer) punchAnswerEnvelope.getMessage() : null);
            if (answer == null) {
                String reason = "Received no answer for the sent UDP Punch Message [to establish relay]!";
                Logger.logWarning(reason);
                sendRelayAnswer((SocketChannel) env.getChannel(), env.getSender(), false, reason, null);
                return;
            }
            if (!answer.isSuccessful()) {
                String reason = "Punching denied! Reason: " + answer.getReason();
                Logger.logWarning(reason);
                sendRelayAnswer((SocketChannel) env.getChannel(), env.getSender(), false, reason, null);
                return;
            }
            commFacade.getChannelManager().registerChannel(relayTarget);
            commFacade.getChannelManager().registerChannel(requestor);
            ping = waitForUDPPing.waitForMessage();
        } finally {
            commFacade.getMessageProcessor().removeHook(waitForPunchAnswer);
            commFacade.getMessageProcessor().removeHook(waitForUDPPing);
        }
        if (ping == null) {
            String reason = "Punching should be successful, but we couldnt receive the PING!";
            Logger.logWarning(reason);
            sendRelayAnswer((SocketChannel) env.getChannel(), env.getSender(), false, reason, null);
            return;
        }
        InetSocketAddress useAddress = new InetSocketAddress(InetAddressUtils.getMostProbableExternalAddress(), requestor.socket().getLocalPort());
        IEnvelope relayAnswerACK = waitForRelayAnswerACK((SocketChannel) env.getChannel(), requestor, env.getSender(), useAddress);
        if (relayAnswerACK == null) {
            String reason = "The requestor of the relay has not sent a RelayAnswerACK!";
            Logger.logWarning(reason);
            return;
        }
        try {
            relayTarget.connect(ping.getSender().getAddress());
            requestor.connect(relayAnswerACK.getSender().getAddress());
        } catch (IOException e) {
        }
        Logger.log("Successfully established relay: " + requestor.socket().getRemoteSocketAddress() + " -> " + " [ " + requestor.socket().getLocalSocketAddress() + " , " + relayTarget.socket().getLocalSocketAddress() + " ] " + relayTarget.socket().getRemoteSocketAddress());
        daemon.addMapping(requestor, relayTarget);
        try {
            commFacade.sendUDPMessage(requestor, new RelayStartMessage(myPeerID, requestorID));
        } catch (IOException e) {
        }
    }
