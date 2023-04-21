    public Channel getChannel(String handle) throws NoSuchChannelException {
        Channel channel = channelSuite.getChannel(handle);
        if (channel == null) {
            throw new NoSuchChannelException(this, handle);
        }
        return channel;
    }
