    int[] _checkRouteCache(long src_, long dest_, int incomingIf_) {
        if (routeCache == null || routeCache.cache == null) return null;
        int top_ = 0;
        int bottom_ = routeCache.cache.size() - 1;
        while (top_ <= bottom_) {
            int i = (top_ + bottom_) / 2;
            RouteCacheEntry cache_ = (RouteCacheEntry) routeCache.cache.get(i);
            if (cache_.dest == dest_) {
                if (cache_.match(src_, incomingIf_)) {
                    routeCache.routeHitCount++;
                    return cache_.ifs;
                }
                for (ListIterator it_ = routeCache.cache.listIterator(i); it_.hasPrevious(); ) {
                    if (cache_.dest == dest_) {
                        if (cache_.match(src_, incomingIf_)) {
                            routeCache.routeHitCount++;
                            return cache_.ifs;
                        }
                    } else break;
                }
                for (ListIterator it_ = routeCache.cache.listIterator(i + 1); it_.hasNext(); ) {
                    if (cache_.dest == dest_) {
                        if (cache_.match(src_, incomingIf_)) {
                            routeCache.routeHitCount++;
                            return cache_.ifs;
                        }
                    } else break;
                }
                return null;
            } else if (cache_.dest > dest_) bottom_ = i - 1; else top_ = i + 1;
        }
        return null;
    }
