    public Object go(ProceedingJoinPoint jp, Cached cached) throws Throwable {
        Object result;
        long now = 0;
        if (logger.isDebugEnabled()) {
            now = System.nanoTime();
        }
        try {
            Method m = ((MethodSignature) jp.getSignature()).getMethod();
            Serializable key = getKeyGenerator(cached).generateKey(cached, jp.getArgs(), m, jp.getSignature().toLongString());
            int ttl = -777;
            boolean wasCached;
            String cacheName = getCacheSelector(cached).getCacheName(cached, jp.getArgs(), m, jp.getSignature().toLongString());
            AnnoCache<E> cache = null;
            if (cacheName == null) {
                cache = cacheManager.getCache("default");
            } else {
                cache = cacheManager.getCache(cacheName);
            }
            if (cache != null) {
                E element = cache.get(key);
                if (element == null) {
                    element = cache.generateElement(key, jp.proceed());
                    ttl = getTTLGenerator(cached).getTTL(cached, jp.getArgs(), m);
                    if (ttl >= 0) {
                        cache.setTtl(element, ttl);
                    }
                    cache.put(element);
                    wasCached = false;
                } else {
                    wasCached = true;
                }
                result = cache.getValue(element);
            } else {
                wasCached = true;
                throw new CacheException("no cache found with name " + cacheName);
            }
            if (info.get() != null && !info.get().set) {
                info.get().cacheKey = key;
                info.get().cacheKeyGenerator = getKeyGenerator(cached).getClass();
                info.get().cacheName = cacheName;
                info.get().fromFache = wasCached;
                info.get().ttl = ttl;
                info.get().ttlGenerator = getTTLGenerator(cached).getClass();
                info.get().cacheSelector = getCacheSelector(cached).getClass();
                info.get().nano = now - System.nanoTime();
            }
        } catch (CacheException e) {
            if (throwsOnCacheError) {
                throw new RuntimeException(e);
            } else {
                logger.warn("could not read or write from cache", e);
                if (info.get() != null && !info.get().set) {
                    info.get().ex = e;
                }
                result = jp.proceed();
            }
        } finally {
            if (info.get() != null) {
                info.get().set = true;
            }
        }
        if (logger.isDebugEnabled()) {
            long duration = System.nanoTime() - now;
            logger.debug(jp.toLongString() + " took " + (duration) + " nanoseconds");
        }
        return result;
    }
