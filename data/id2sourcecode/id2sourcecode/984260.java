    public XML_Parser(String what, boolean isXML) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser parser = saxParserFactory.newSAXParser();
        DefaultHandler handler = new XML_ParserHandler();
        InputStream input = null;
        if (!isXML) {
            URL url = new URL(what);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(XML_Parser.REQUEST_TIMEOUT);
            connection.connect();
            input = connection.getInputStream();
        } else {
            input = new ByteArrayInputStream(what.getBytes());
        }
        parser.parse(input, handler);
        data = ((XML_ParserHandler) handler).getData();
    }
