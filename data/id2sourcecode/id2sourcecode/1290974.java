    private static final void load(Map labels, URL url) throws IOException {
        log.info(MCommon.FILE_OPENING, url);
        final Map news = new HashMap();
        Maps.load(news, url.openStream());
        for (Iterator it = news.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry me = (Map.Entry) it.next();
            final Object key = me.getKey();
            if (labels.put(key, me.getValue()) != null) log.warning("Label of " + key + " is replaced by " + url);
        }
    }
