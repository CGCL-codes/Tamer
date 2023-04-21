    private static Document parseDocument(URL url) throws ParserConfigurationException, SAXException, IOException {
        LOGGER.debug("Loading XML data from URL \"" + url + "\"");
        return parseDocument(url.openStream(), url.toExternalForm());
    }
