    public Channel getChannel(String chanHandle) throws NoSuchChannelException {
        Channel channel = channelSuite.getChannel(chanHandle);
        if (channel == null) {
            throw new NoSuchChannelException(this, chanHandle);
        }
        return channel;
    }
