    private void requestNewConnection(String name) {
        channel = ChannelFactory.defaultFactory().getChannel(name);
        channel.addConnectionListener(this);
        channel.requestConnection();
    }
