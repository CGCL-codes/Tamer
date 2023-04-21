    private void readCheckers(final URL url) throws SAXException, IOException {
        readCheckers(documentBuilder.parse(url.openStream()));
    }
