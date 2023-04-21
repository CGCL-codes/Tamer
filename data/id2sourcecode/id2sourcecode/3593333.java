    public PluginAttributes parse() throws ParserConfigurationException, SAXException, IOException {
        attributes.clear();
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        InputStream inputStream = url.openStream();
        parser.parse(inputStream, this);
        inputStream.close();
        return current;
    }
