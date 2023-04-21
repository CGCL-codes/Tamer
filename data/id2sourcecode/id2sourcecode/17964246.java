    public InputStream getResourceAsStream(String name) {
        URL url = getResource(name);
        if (url != null) {
            try {
                return url.openStream();
            } catch (IOException e) {
                log("Error opening stream for resource '" + name + "'", e);
            }
        }
        return null;
    }
