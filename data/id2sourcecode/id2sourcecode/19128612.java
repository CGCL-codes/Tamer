    @Override
    public InputStream getResourceAsStream(String ref) {
        try {
            URL url = Platform.getBundle(bundleID).getResource(ref);
            if (url == null) {
                throw new RuntimeException("Failed to load resource: " + ref);
            }
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource.", e);
        }
    }
