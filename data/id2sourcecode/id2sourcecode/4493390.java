    public static InputStream getResourceAsStream(String resourceName) {
        URL url = getResource(resourceName);
        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException e) {
        }
        return null;
    }
