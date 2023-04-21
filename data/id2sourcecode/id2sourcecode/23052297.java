    private DomainMapImpl() {
        super();
        java.net.URL url = null;
        try {
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setFeature("http://xml.org/sax/features/namespaces", true);
            reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
            reader.setContentHandler(new MapContentHandler(this, reader, null, null));
            url = Thread.currentThread().getContextClassLoader().getResource("domain-oid-map.xml");
            if (url == null) throw new RuntimeException("domain-oid-map.xml file not found in classpath");
            reader.parse(new InputSource(url.openStream()));
        } catch (Throwable ex) {
            throw new RuntimeException(url.toString() + ": " + ex.getMessage(), ex);
        }
    }
