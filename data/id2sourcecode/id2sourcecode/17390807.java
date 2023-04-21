    public static Config parse(Reader reader, Config config) throws SAXException, IOException {
        XMLReader xml_reader = XMLReaderFactory.createXMLReader();
        ConfigXMLContentHandler ch = new ConfigXMLContentHandler(config);
        try {
            xml_reader.setFeature("http://apache.org/xml/features/validation/schema", true);
            xml_reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
            URL url = Thread.currentThread().getContextClassLoader().getResource("mpeg7audioenc.xsd");
            if (url != null) {
                assert Debug.println(System.err, "Using mpeg7audioenc.xsd included in CLASSPATH");
                xml_reader.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", url.openStream());
            } else {
                assert Debug.println(System.err, "Can't open local copy of mpeg7audioenc.xsd");
            }
        } catch (SAXNotRecognizedException e) {
            assert Debug.println(System.err, "SAXNotRecognizedException:" + e.getMessage());
        }
        xml_reader.setContentHandler(ch);
        xml_reader.setErrorHandler(ch);
        xml_reader.parse(new InputSource(reader));
        return ch.getConfig();
    }
