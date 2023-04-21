    public static InputStream getResourceAsStream(String resourceName, ClassLoader classLoader) {
        URL url = getResource(resourceName, classLoader);
        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException e) {
        }
        return null;
    }
