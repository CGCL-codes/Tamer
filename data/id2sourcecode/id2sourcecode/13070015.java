    public DOMResource(URL url) throws EmbedException, URISyntaxException, IOException, SAXException, ParserConfigurationException {
        this(readDocument(url.openStream()), url.toURI());
    }
