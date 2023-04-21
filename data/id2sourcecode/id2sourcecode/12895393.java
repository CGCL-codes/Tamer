    public Channel getChannel(Channel channel) {
        return new Channel(channel.address, getChannelValue(channel.address));
    }
