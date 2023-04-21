    public int loadCache() throws IOException {
        URL url = JConfig.getURL(this.name, RESOLVER_SEARCH_PATH_PROPERTY);
        if (url == null) {
            logger.fine("cache file " + name + " not found");
            return 0;
        }
        logger.finer("loading cache file " + url.toString());
        return loadCache(new BufferedReader(new InputStreamReader(url.openStream())));
    }
