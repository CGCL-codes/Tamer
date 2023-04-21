    private void onReceiveControlIntroduction(IEnvelope env, ControlIntroductionMessage message) {
        if (controlChannelMappings.containsKey(message.getSenderPeerID())) {
            try {
                env.getChannel().close();
            } catch (IOException e) {
            }
            return;
        }
        controlChannelMappings.put(message.getSenderPeerID(), (SocketChannel) env.getChannel());
        ControlIntroductionAnswer answer = new ControlIntroductionAnswer(connBroker.getCallback().getOwnPeerID(), env.getSender().getPeerID());
        try {
            commFacade.sendTCPMessage((SocketChannel) env.getChannel(), answer);
        } catch (IOException e) {
        }
    }
