    private void receiveJoinAnswer(IEnvelope env, JoinChatAnswer message) {
        ChatPartner cp = new ChatPartner(message.getNickname(), env.getSender().getPeerID(), message.getAddress(), env.getSender().getAddress());
        cp.setChannel((DatagramChannel) env.getChannel());
        chatModel.addChatPartner(cp);
        chatModel.addToStayAlive(cp);
        if (!message.getRecognizedAddress().getAddress().equals(InetAddressUtils.getMostProbableExternalAddress().getAddress())) connBroker.getControlChannelManager().createControlChannel(env.getSender());
    }
