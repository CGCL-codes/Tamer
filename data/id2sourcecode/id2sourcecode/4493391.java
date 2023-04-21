    public static InputStream getResourceAsStream(String resourceName, Class referrer) {
        URL url = getResource(resourceName, referrer);
        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException e) {
        }
        return null;
    }
