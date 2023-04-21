    private static Properties loadProperties(URL url, String info) {
        if (log.isInfoEnabled()) {
            log.info("MemCache: Getting properties from URL " + url + " for " + info);
        }
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = url.openStream();
            properties.load(in);
            if (log.isInfoEnabled()) {
                log.info("MemCache: Properties read " + properties);
            }
        } catch (Exception e) {
            log.error("MemCache: Error reading from " + url, e);
            log.error("MemCache: Ensure the properties information in " + url + " is readable and in your classpath.");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.warn("MemCache: IOException while closing InputStream: " + e.getMessage());
            }
        }
        return properties;
    }
