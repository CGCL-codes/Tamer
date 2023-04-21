    protected static Properties loadProperties(URL url) {
        Properties result = new Properties();
        logger.debug("Reading configuration from URL " + url);
        try {
            result.load(url.openStream());
        } catch (java.io.IOException e) {
            logger.error("Could not read configuration file from URL [" + url + "].", e);
        }
        return result;
    }
