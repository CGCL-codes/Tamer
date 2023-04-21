    public final Channel getChannel(final ContentManager.ChannelSpecification.Key channelKey) {
        return (channelKey != null) ? getChannel(channelKey.getChannelID()) : null;
    }
