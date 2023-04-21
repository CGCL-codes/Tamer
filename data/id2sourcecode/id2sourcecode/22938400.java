    protected Templates getTemplatesFromCache(URL url) throws PrintException {
        Templates templates = null;
        Cache cache = CacheManager.getInstance().getCache(this.getClass().getName());
        try {
            templates = (Templates) cache.get(url);
        } catch (CacheException e) {
            throw new PrintException(e.getMessage(), e);
        }
        if (log.isDebugEnabled()) {
            if (templates != null) log.debug("took " + url + " from cache");
        }
        if (templates == null) {
            StreamSource inputSource = null;
            try {
                inputSource = new StreamSource(new InputStreamReader(url.openStream(), "utf-8"), url.toExternalForm());
                if (log.isDebugEnabled()) {
                    log.debug("read " + url + " to cache");
                }
            } catch (IOException e) {
                throw new PrintException("IO Fehler templateURL " + e.getMessage(), e);
            }
            try {
                templates = transformerFactory.newTemplates(inputSource);
            } catch (TransformerConfigurationException e) {
                throw new RuntimeException("Initialisierung Transformerfactory " + url, e);
            }
            try {
                cache.add(url, templates);
            } catch (CacheException e) {
                throw new PrintException(e.getMessage(), e);
            }
        }
        return templates;
    }
