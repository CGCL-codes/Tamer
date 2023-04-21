    @Override
    protected InputStream getDecoratedInputStream(InputStream inputStream) throws IOException {
        return new URL(url).openStream();
    }
