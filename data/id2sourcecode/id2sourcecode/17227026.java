    public InputStream getResourceAsStream(String path) {
        try {
            URL url = this.getResource(path, false);
            return url == null ? null : url.openStream();
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
