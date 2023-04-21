    private InputSource getInputSource(String endpoint, Collection psis) throws IOException {
        String uri = addParameters(endpoint, psis);
        URL url = new URL(uri);
        InputSource src = new InputSource(uri);
        src.setByteStream(url.openStream());
        return src;
    }
