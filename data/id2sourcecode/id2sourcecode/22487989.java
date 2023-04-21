    public InputStream openStream(URL url) throws IOException {
        prepareURL(url);
        return delegate.openStream(url);
    }
