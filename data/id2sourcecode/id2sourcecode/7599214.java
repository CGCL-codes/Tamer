    public void onShutdown() {
        for (Iterator i = chatChannels.values().iterator(); i.hasNext(); ) {
            ChatChannel channel = (ChatChannel) i.next();
            final int channelName = getChatChannel(channel.getName()).getChannelId();
            ChatterProcessor chatterProcessor = new ChatterProcessor(new Long(-1), channel);
            chatterProcessor.doProcessor(new Processor() {

                public AbstractAction getAction(User user, User newUser) {
                    ClientTransceiver clientTransceiver = new ClientTransceiver(user.getNetworkChannel());
                    clientTransceiver.addReceiver(user.getNetworkChannel());
                    ChatCloseAction action = new ChatCloseAction(user.getUniqueId(), channelName);
                    action.setTransceiver(clientTransceiver);
                    return action;
                }
            });
        }
        super.onShutdown();
    }
