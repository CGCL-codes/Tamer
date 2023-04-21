    @Override
    public InputStream fetch(URL url) throws IOException {
        return url.openStream();
    }
