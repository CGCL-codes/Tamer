    public static final DataDirConfig getDefault() {
        if (ref == null) {
            try {
                InputStream propStream = null;
                URL url = Thread.currentThread().getContextClassLoader().getResource(PROPERTY_FILE);
                if (url != null) {
                    log.info("Building DataDirConfig singleton from properties: " + url.toURI());
                    try {
                        propStream = url.openStream();
                        ref = new DataDirConfig(propStream);
                    } catch (IOException e) {
                        log.warn("Error reading DataDirConfig properties", e);
                    }
                } else {
                    log.info("Couldn't find the " + PROPERTY_FILE + " resource for building a AppConfig singleton.");
                }
            } catch (Exception e) {
                throw new IllegalStateException("Cannot read application configuration " + PROPERTY_FILE, e);
            }
        }
        return ref;
    }
