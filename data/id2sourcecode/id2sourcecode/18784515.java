    @Override
    protected Channel createChannel() {
        Channel channel = getGravityConfig().getChannelFactory().newChannel(UUIDUtil.randomUUID());
        Expiration expiration = Expiration.byDeltaMillis((int) getGravityConfig().getChannelIdleTimeoutMillis());
        gaeCache.put(CHANNEL_PREFIX + channel.getId(), channel, expiration);
        gaeCache.put(GAEChannel.MSG_COUNT_PREFIX + channel.getId(), 0L, expiration);
        return channel;
    }
