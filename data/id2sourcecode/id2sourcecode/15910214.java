    public static InputStream getResourceAsStream(String resourceName) {
        URL url = getResource(resourceName);
        try {
            return (url != null) ? url.openStream() : null;
        } catch (IOException e) {
            return null;
        }
    }
