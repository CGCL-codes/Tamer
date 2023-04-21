    protected XMLEventReader open(String url) throws IOException, XMLStreamException {
        return this.xmlInputFactory.createXMLEventReader(openStream(url));
    }
