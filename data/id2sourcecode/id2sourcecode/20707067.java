    public void build(URL url, Container target) throws IOException {
        build(url.toExternalForm(), new InputSource(url.openStream()), target);
    }
