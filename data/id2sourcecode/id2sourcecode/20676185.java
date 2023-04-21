    public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
        URL url = getResource(resourceName, callingClass);
        try {
            return (url != null) ? url.openStream() : null;
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.debug(e);
            }
            return null;
        }
    }
