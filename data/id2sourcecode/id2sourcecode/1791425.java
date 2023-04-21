    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> Channel<T> getChannel(String channelName, boolean persistent) {
        LOG.fine("Creating channel: " + channelName);
        final String namespace = ChannelServiceFactory.getNamespace(channelName);
        final MemcacheService service = MemcacheServiceFactory.getMemcacheService(namespace);
        final NamedCounter rOffset = new AppEngineCounter(service, "R");
        final NamedCounter wOffset = new AppEngineCounter(service, "W");
        final Map props = new HashMap();
        props.put(GCacheFactory.EXPIRATION_DELTA, 10 * 3600);
        props.put(GCacheFactory.MEMCACHE_SERVICE, namespace);
        Cache cache = null;
        final CacheManager manager = CacheManager.getInstance();
        while (cache == null) {
            try {
                manager.registerCache(namespace, manager.getCacheFactory().createCache(props));
            } catch (CacheException e) {
                e.printStackTrace();
            }
            cache = manager.getCache(namespace);
        }
        return new ChannelImpl<T>(namespace, cache, rOffset, wOffset, persistent);
    }
