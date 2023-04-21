    @Override
    public <T extends Serializable> Channel<T> getChannel(String channelName, boolean persistence) {
        final String namespace = ChannelServiceFactory.getNamespace(channelName);
        Cache cache = null;
        final CacheManager manager = CacheManager.getInstance();
        while (cache == null) {
            try {
                manager.registerCache(namespace, manager.getCacheFactory().createCache(Collections.emptyMap()));
            } catch (CacheException e) {
                e.printStackTrace();
            }
            cache = manager.getCache(namespace);
        }
        final NamedCounter rOffset = new JCacheCounter(cache, "R");
        final NamedCounter wOffset = new JCacheCounter(cache, "W");
        return new ChannelImpl<T>(namespace, cache, rOffset, wOffset, persistence);
    }
