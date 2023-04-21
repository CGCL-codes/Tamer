    @Override
    public InputStream openStream() throws IOException {
        return new GetContent().openStream(url);
    }
