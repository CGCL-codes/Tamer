    @Override
    public InputStream getResourceAsStream(String name) {
        try {
            URL url = this.getResource(name);
            return url == null ? null : url.openStream();
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
