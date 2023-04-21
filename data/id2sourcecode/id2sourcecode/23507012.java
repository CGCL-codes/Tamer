    public final void addChannel(String channelId, RecordFilter recordFilter) {
        if (hasSource(channelId)) return;
        Channel channel = ChannelFactory.defaultFactory().getChannel(channelId);
        addChannel(channel, recordFilter);
    }
