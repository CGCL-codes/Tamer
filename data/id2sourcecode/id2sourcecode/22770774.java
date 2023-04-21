    PropertyFileUrlMap(final String urlDomain, final Properties map) {
        syslog = Logger.getLogger(getClass().getName());
        writeThreadCheckInterval = Long.MAX_VALUE;
        backingFile = null;
        writeThread = null;
        shortener = new BrokenSoManyWaysShortenerStrategy(this);
        urlMap = Preconditions.checkNotNull(map);
    }
