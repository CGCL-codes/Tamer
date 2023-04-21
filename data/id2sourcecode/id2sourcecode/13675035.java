    @Override
    public InputStream newInputStream() throws IOException {
        return url.openStream();
    }
