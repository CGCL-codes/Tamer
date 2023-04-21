    private InputStream getInputStream(String uri) {
        InputStream stream;
        URL url = getClass().getResource(uri);
        if (url != null) {
            try {
                stream = url.openStream();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            throw new IllegalArgumentException("Invalid Bluprints location [" + uri + "]. Must be in the classpath.");
        }
        return stream;
    }
