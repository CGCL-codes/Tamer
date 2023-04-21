    @Override
    public Channel getChannel(String channelId) {
        if (channelId == null) return null;
        return (Channel) gaeCache.get(CHANNEL_PREFIX + channelId);
    }
