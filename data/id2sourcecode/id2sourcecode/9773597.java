    public void parse() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        MySaxHandler handler = new MySaxHandler();
        reader.setContentHandler(handler);
        InputSource input = new InputSource(url.openStream());
        reader.parse(input);
        this.indexObject = handler.getIndexObject();
    }
