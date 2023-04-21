    public void onMessageReceived(IEnvelope env) {
        if ((env.getMessage() instanceof UDPPing) || (env.getMessage() instanceof UDPPong)) {
            onReceivedPingPong(env);
            return;
        } else if (env.getMessage() instanceof StayAliveMessage) {
            if (((StayAliveMessage) env.getMessage()).needAnswer()) {
                try {
                    IMessage msg = new StayAliveMessage(myself.getPeerID(), env.getSender().getPeerID(), false);
                    commFacade.sendUDPMessage((DatagramChannel) env.getChannel(), msg, env.getSender().getAddress());
                } catch (IOException e) {
                }
            }
            return;
        }
        IMessage msg = env.getMessage();
        if (msg.getSenderPeerID().equals(getMyself().getPeerID())) return;
        if (msg instanceof ChatMessage) {
            onReceivedChatMsg(env, msg);
        } else if (msg instanceof KnownHostsMessage) {
            onReceiveKnownHosts(env, msg);
        }
    }
