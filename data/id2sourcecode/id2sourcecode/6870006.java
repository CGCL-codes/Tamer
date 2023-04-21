    public void unsubscribe(String toChannel, Client subscriber) {
        ChannelImpl channel = (ChannelImpl) getChannel(toChannel);
        if (channel != null) channel.unsubscribe(subscriber);
    }
