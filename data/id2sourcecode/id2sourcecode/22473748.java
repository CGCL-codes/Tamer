    public InputStream loadContext(URL url) throws ContextLoaderException {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new ContextLoaderException(e);
        }
    }
