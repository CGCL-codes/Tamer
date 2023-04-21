    public static InputStream getInputStream(String resourceName) throws IOException {
        InputStream stream = null;
        URL url = getUrl(resourceName);
        if (url != null) {
            stream = url.openStream();
        }
        return stream;
    }
