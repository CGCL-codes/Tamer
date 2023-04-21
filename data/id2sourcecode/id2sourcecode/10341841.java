    public ChannelWrapper(String pv) {
        _channel = ChannelFactory.defaultFactory().getChannel(pv);
        _connectionHandler = new ConnectionHandler();
        _channel.addConnectionListener(_connectionHandler);
    }
