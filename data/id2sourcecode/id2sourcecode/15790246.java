    private void receiveJoinMessage(IEnvelope env, JoinChatMessage message) {
        if (chatModel.getChatPartner(env.getSender().getPeerID()) == null) {
            ChatPartner cp = new ChatPartner(message.getNickname(), env.getSender().getPeerID(), message.getAddress(), env.getSender().getAddress());
            cp.setChannel((DatagramChannel) env.getChannel());
            chatModel.addChatPartner(cp);
        }
        IChatPartner me = chatModel.getMyself();
        IMessage msg = new JoinChatAnswer(myself.getPeerID(), env.getSender().getPeerID(), me.getNickname(), me.getPeer().getAddress(), env.getSender().getAddress());
        try {
            commFacade.sendUDPMessage((DatagramChannel) env.getChannel(), msg, env.getSender().getAddress());
        } catch (IOException e) {
            Logger.logError(e, "Could not send JoinAnswer back.");
        }
    }
