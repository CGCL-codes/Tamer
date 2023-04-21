    public InputStream openStream() throws IOException {
        return new GZIPInputStream(url.openStream());
    }
