    protected Node loadDocument(URL url) throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException, IOException, SAXException {
        return loadDocument(url.openStream());
    }
