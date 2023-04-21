    public void removeChannelCache(ChannelIF channel) {
        if (channel == null) return;
        String key = getChannelCacheKey(channel);
        String actualKey = cacheAdmin.generateEntryKey(key, null, PageContext.APPLICATION_SCOPE);
        cache.removeEntry(actualKey);
    }
