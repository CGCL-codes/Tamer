    @Override
    protected void postManage(Channel channel) {
        Expiration expiration = Expiration.byDeltaMillis((int) getGravityConfig().getChannelIdleTimeoutMillis());
        gaeCache.put(CHANNEL_PREFIX + channel.getId(), channel, expiration);
    }
