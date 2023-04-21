    public Channel registerPlayerWithChannel(ChatClient chatClient, int channelIndex, byte[] channelIdentifier) {
        Channel channel = Channels.getChannelByIdentifier(channelIdentifier);
        if (channel != null) chatClient.addChannel(channel);
        return channel;
    }
