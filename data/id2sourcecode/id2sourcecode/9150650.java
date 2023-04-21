    private Document getDocument(final String request) throws JDOMException, IOException {
        final URL url = new URL(request);
        return new SAXBuilder().build(new InputStreamReader(url.openStream()));
    }
