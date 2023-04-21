    public static void initialize() {
        if (INITIALIZED) return;
        INITIALIZED = true;
        String defaultConfiguration = "log4j.xml";
        URL url = null;
        final String configuration = loggingConfigurationUri;
        if (configuration != null) {
            try {
                url = new URL(configuration);
                InputStream stream = url.openStream();
                stream.close();
                stream = null;
                configure(url);
                logger.info("Logging configured from external source --> '" + configuration + "'");
            } catch (Throwable throwable) {
                url = DTULogger.class.getResource(defaultConfiguration);
                configure(url);
                logger.warn("Invalid logging configuration uri --> '" + configuration + "'");
            }
        }
        if (url == null) {
            url = DTULogger.class.getResource(defaultConfiguration);
            configure(url);
        }
        if (url == null) {
            throw new RuntimeException("Could not find default logging configuration file '" + defaultConfiguration + "'");
        }
    }
