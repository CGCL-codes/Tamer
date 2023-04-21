    public Channel getChannel(short address) {
        return new Channel(address, getChannelValue(address));
    }
