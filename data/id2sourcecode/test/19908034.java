    public XmlTaskSource(URL url, URL baseURL, Config config) throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
        this(url.openStream(), baseURL, config);
    }
