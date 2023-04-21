    private static void _initializeBeanTypes(URL url) {
        try {
            Properties properties = new Properties();
            InputStream is = url.openStream();
            try {
                properties.load(is);
                if (_LOG.isFine()) _LOG.fine("Loading bean factory info from " + url);
                _TYPES_MAP.putAll(properties);
            } finally {
                is.close();
            }
        } catch (IOException ioe) {
            _LOG.severe("CANNOT_LOAD_URL", url);
            _LOG.severe(ioe);
        }
    }
