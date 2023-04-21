    public static SimpleHtmlDocument simpleParse(URL url) throws SAXException, IOException {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        InputStream in = url.openStream();
        return simpleParse(in);
    }
