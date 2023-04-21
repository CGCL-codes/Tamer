    public InputStream getResource() {
        try {
            return url.openStream();
        } catch (IOException ex) {
            return null;
        }
    }
