    @Override
    public InputStream getResourceAsStream(String resourceName) {
        InputStream is = super.getResourceAsStream(resourceName);
        if (is == null) {
            try {
                URL url = this.getResource(resourceName);
                return url.openStream();
            } catch (IOException e) {
                throw ThrowableManagerRegistry.caught(e);
            }
        }
        return is;
    }
