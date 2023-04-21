    public void flushChannelCache(ChannelIF channel) {
        if (channel == null) return;
        String key = getChannelCacheKey(channel);
        String actualKey = cacheAdmin.generateEntryKey(key, null, PageContext.APPLICATION_SCOPE);
        cache.flushEntry(actualKey);
    }
