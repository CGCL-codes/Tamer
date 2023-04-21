    @SuppressWarnings("unchecked")
    public StringBuffer getResource(String resource, boolean fromWeb, boolean cacheContent) {
        try {
            URL url = fromWeb ? context.getResource(resource) : ControllerServlet.class.getResource(resource);
            URLConnection con = url.openConnection();
            if (cacheContent) {
                HashMap<String, StringBuffer> cache = (HashMap<String, StringBuffer>) context.getAttribute(CACHE);
                HashMap<String, Long> cacheTimes = (HashMap<String, Long>) context.getAttribute(CACHE_TIMES);
                if (cache == null) {
                    cache = new HashMap<String, StringBuffer>();
                    cacheTimes = new HashMap<String, Long>();
                    context.setAttribute(CACHE, cache);
                    context.setAttribute(CACHE_TIMES, cacheTimes);
                }
                long lastModified = con.getLastModified();
                long cacheModified = 0;
                if (cacheTimes.get(resource) != null) {
                    cacheModified = ((Long) cacheTimes.get(resource)).longValue();
                }
                if (cacheModified < lastModified) {
                    StringBuffer buffer = getResource(con.getInputStream());
                    synchronized (cacheTimes) {
                        cacheTimes.put(resource, Long.valueOf(lastModified));
                    }
                    synchronized (cache) {
                        cache.put(resource, buffer);
                    }
                    return buffer;
                } else {
                    return (StringBuffer) cache.get(resource);
                }
            } else {
                return getResource(con.getInputStream());
            }
        } catch (Exception e) {
            PetstoreUtil.getLogger().log(Level.SEVERE, "ControllerServlet:loadResource error: Could not load", resource + " - " + e.toString());
        }
        return null;
    }
