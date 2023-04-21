    private void notifyMembersMessage(final String message, final ChatChannel chatChannel, final Long userId) {
        ChatterProcessor chatterProcessor = new ChatterProcessor(userId, chatChannel);
        chatterProcessor.doProcessor(new Processor() {

            public AbstractAction getAction(User user, User newUser) {
                ClientTransceiver clientTransceiver = new ClientTransceiver(user.getNetworkChannel());
                clientTransceiver.addReceiver(user.getNetworkChannel());
                ChatMessageAction action = new ChatMessageAction(message, chatChannel.getChannelId(), userId.longValue(), newUser.getName());
                action.setTransceiver(clientTransceiver);
                return action;
            }
        });
    }
