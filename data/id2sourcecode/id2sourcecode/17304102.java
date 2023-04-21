    void _updateRouteCache(long src_, long dest_, int incomingIf_, int[] ifs_) {
        if (routeCache == null) routeCache = new RouteCache();
        if (routeCache.cache == null) {
            routeCache.cache = new LinkedList();
            routeCache.cache.add(new RouteCacheEntry(src_, dest_, incomingIf_, ifs_));
            return;
        }
        int top_ = 0;
        int bottom_ = routeCache.cache.size() - 1;
        int added_ = -1;
        while (top_ <= bottom_) {
            int i = (top_ + bottom_) / 2;
            RouteCacheEntry cache_ = (RouteCacheEntry) routeCache.cache.get(i);
            if (cache_.dest == dest_) {
                routeCache.cache.add(i, new RouteCacheEntry(src_, dest_, incomingIf_, ifs_));
                added_ = i;
                break;
            } else if (cache_.dest > dest_) bottom_ = i - 1; else top_ = i + 1;
        }
        if (added_ < 0) routeCache.cache.add(top_, new RouteCacheEntry(src_, dest_, incomingIf_, ifs_));
        if (routeCache.cache.size() > routeCache.CACHE_SIZE) routeCache.cache.remove((int) (Math.random() * routeCache.CACHE_SIZE));
    }
