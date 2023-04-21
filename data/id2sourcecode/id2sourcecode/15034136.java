    public void start(ContentHandler sax, Target target) throws IOException, SAXException {
        URLConnection conn = url.openConnection();
        InputStream fis = conn.getInputStream();
        XMLReader parser = new org.apache.xerces.parsers.SAXParser();
        parser.setFeature("http://xml.org/sax/features/validation", false);
        parser.setFeature("http://xml.org/sax/features/external-general-entities", true);
        parser.setFeature("http://xml.org/sax/features/namespaces", true);
        parser.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
        parser.setContentHandler(sax);
        parser.parse(new InputSource(fis));
        fis.close();
    }
