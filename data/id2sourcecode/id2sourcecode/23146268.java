    public void parse() throws SAXException, ParserConfigurationException, IOException {
        if (parserFactory == null) {
            parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
        }
        XMLReader parser = parserFactory.newSAXParser().getXMLReader();
        parser.setContentHandler(handler);
        parser.setDTDHandler(handler);
        parser.setEntityResolver(handler);
        parser.setErrorHandler(handler);
        InputSource inputSource = new InputSource(url.openStream());
        inputSource.setSystemId(url.toString());
        parser.parse(inputSource);
    }
