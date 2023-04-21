    public static InputStream getResourceAsStream(String resourceName, Class callingClass) {
        URL url = getResource(resourceName, callingClass);
        try {
            return url == null ? null : url.openStream();
        } catch (IOException e) {
            return null;
        }
    }
