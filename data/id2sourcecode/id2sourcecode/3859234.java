    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> Channel<T> getChannel(String channelName, boolean persistent) {
        LOG.fine("Creating channel: " + channelName);
        final String namespace = getChannelNamespace(channelName);
        final MemcacheService service = MemcacheServiceFactory.getMemcacheService(namespace);
        final NamedCounter rOffset = new MemCacheCounter(service, "R");
        final NamedCounter wOffset = new MemCacheCounter(service, "W");
        final Map props = new HashMap();
        props.put(GCacheFactory.EXPIRATION_DELTA, 10 * 3600);
        props.put(GCacheFactory.MEMCACHE_SERVICE, namespace);
        Cache cache = null;
        final CacheManager manager = CacheManager.getInstance();
        while (cache == null) {
            cache = manager.getCache(namespace);
            try {
                manager.registerCache(namespace, manager.getCacheFactory().createCache(props));
            } catch (CacheException e) {
                e.printStackTrace();
            }
        }
        return new ChannelImpl<T>(namespace, cache, rOffset, wOffset, persistent);
    }
