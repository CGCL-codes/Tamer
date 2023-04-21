    public InputStream getResourceAsStream(String path) {
        try {
            URL url = getResource(path);
            if (url != null) {
                return url.openStream();
            }
        } catch (Exception e) {
        }
        return null;
    }
