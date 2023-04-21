    @Override
    public void deleteChannel(String channelName) {
        LOG.fine("Deleting channel: " + channelName);
        final String CHANNEL_NAMESPACE = getChannelNamespace(channelName);
        final MemcacheService memCache = MemcacheServiceFactory.getMemcacheService(CHANNEL_NAMESPACE + channelName);
        memCache.clearAll();
    }
