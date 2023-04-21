    public DSN(String server) throws ParserConfigurationException, SAXException, MalformedURLException, IOException, URISyntaxException {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        dsn = new DSNParser();
        parser.parse(URIFactory.url(server + "/das/dsn").openStream(), dsn);
    }
