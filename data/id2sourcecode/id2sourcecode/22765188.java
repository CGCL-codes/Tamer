    public InputStream getResourceAsStream(String name) {
        URL url = findResource(name);
        if (url == null) {
            url = getResource(name);
        }
        if (url != null) {
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
