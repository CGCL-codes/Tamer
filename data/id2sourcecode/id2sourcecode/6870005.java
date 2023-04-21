    public void subscribe(String toChannel, Client subscriber) {
        ChannelImpl channel = (ChannelImpl) getChannel(toChannel, true);
        if (channel != null) channel.subscribe(subscriber);
    }
