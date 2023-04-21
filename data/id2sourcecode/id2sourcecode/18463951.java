    public InputStream getResourceAsStream(String resource) {
        try {
            URL url = getResource(resource);
            if (url != null) {
                return url.openStream();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
