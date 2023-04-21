    public LoggerChannel getChannel(String name) {
        LoggerChannel channel = new LoggerChannelImpl(this, name, false, false);
        channels.add(channel);
        return (channel);
    }
