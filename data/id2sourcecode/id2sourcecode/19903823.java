    public final synchronized DataDirConfig getCfg() {
        if (ref == null) {
            try {
                InputStream propStream = null;
                URL url = getClass().getClassLoader().getResource(PROPERTY_FILE);
                if (url != null) {
                    try {
                        propStream = url.openStream();
                        log.info("Building DataDirConfig singleton from properties: " + url.toURI());
                        ref = new DataDirConfig(propStream);
                    } catch (IOException e) {
                        log.warn("Error reading DataDirConfig properties", e);
                        ref = getDefault();
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException("Cannot read application configuration " + PROPERTY_FILE, e);
            }
        }
        return ref;
    }
