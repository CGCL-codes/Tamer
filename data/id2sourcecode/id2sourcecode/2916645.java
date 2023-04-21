    public void receivedMessage(byte[] message) {
        ChannelManager channelManager = AppContext.getChannelManager();
        Channel chat = channelManager.getChannel("chat");
        chat.send(message);
    }
