    public ChannelIF getChannel() {
        if (channel == null) throw new NullPointerException("ChannelBuilder has not been set!");
        return channel;
    }
