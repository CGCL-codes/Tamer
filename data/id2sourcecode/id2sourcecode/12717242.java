    public Channel getAndConnectChannel(final String handle) throws NoSuchChannelException, ConnectionException {
        final Channel channel = getChannel(handle);
        channel.connectAndWait();
        return channel;
    }
