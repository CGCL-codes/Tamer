    public static SimpleDocument simpleParse(URL url, boolean namespaceAware) throws SAXException, IOException {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        InputStream in = url.openStream();
        return simpleParse(in, namespaceAware);
    }
