    @Override
    protected void runImpl() {
        Channel channel = Channels.getChannelById(channelId);
        Message message = new Message(channel, content, clientChannelHandler.getChatClient());
        BroadcastService.getInstance().broadcastMessage(message);
    }
