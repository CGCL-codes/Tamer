    public void parseWSDL(URL WSDLurl) throws IOException, XMLStreamException {
        InputStream in = WSDLurl.openStream();
        XMLInputFactory factory = null;
        factory = XMLInputFactory.newInstance();
        XMLStreamReader parser = factory.createXMLStreamReader(in);
    }
