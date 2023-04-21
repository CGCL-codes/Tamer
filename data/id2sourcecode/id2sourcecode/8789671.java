    public static InputStream locateAndOpenStream(String resourceName) throws IOException {
        URL url = locateURL(resourceName);
        if (url != null) {
            return url.openStream();
        } else {
            throw new RuntimeException("Error locating resource: " + resourceName);
        }
    }
