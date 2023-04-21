    public Channel getAndConnectChannel(String handle) throws NoSuchChannelException, ConnectionException {
        Channel channel = getChannel(handle);
        channel.connectAndWait();
        return channel;
    }
