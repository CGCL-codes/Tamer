    protected void initialise(boolean enabled, boolean enable_read, boolean enable_write, long size, long not_smaller_than) {
        cache_enabled = enabled && (enable_read || enable_write);
        cache_read_enabled = enabled && enable_read;
        cache_write_enabled = enabled && enable_write;
        cache_size = size;
        cache_files_not_smaller_than = not_smaller_than;
        cache_minimum_free_size = cache_size / 4;
        cache_space_free = cache_size;
        stats = new CacheFileManagerStatsImpl(this);
        cacheStatsAndCleaner();
        if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, "DiskCache: enabled = " + cache_enabled + ", read = " + cache_read_enabled + ", write = " + cache_write_enabled + ", size = " + cache_size + " B"));
    }
