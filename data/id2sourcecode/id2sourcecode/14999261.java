    public Parser resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        InputSource source = core.resolveEntity(publicId, systemId);
        if (source == null) return null;
        if (source.getSystemId() != null) systemId = source.getSystemId();
        URL url = new URL(systemId);
        InputStream stream = url.openStream();
        return new Parser(url, new TidyXMLStreamReader(XMLStreamReaderFactory.create(url.toExternalForm(), stream, true), stream));
    }
