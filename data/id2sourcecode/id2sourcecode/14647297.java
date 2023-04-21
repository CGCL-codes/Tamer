    public static Channel getChannelForXSection(XSection xSection, Channels channels) {
        String channelId = xSection.getChannelId();
        return channels.getChannel(channelId);
    }
