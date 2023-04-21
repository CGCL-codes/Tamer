    public static InputStream getInputStream(String name) throws java.io.IOException {
        URL url = getURL(name);
        if (url != null) {
            return url.openStream();
        }
        return null;
    }
