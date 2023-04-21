    protected Document fetchURL(URL url) throws IOException, SAXException {
        InputStream inputstream = url.openStream();
        InputSource xmlInp = new InputSource(inputstream);
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setValidating(false);
        docBuilderFactory.setExpandEntityReferences(false);
        DocumentBuilder parser = null;
        try {
            parser = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException err) {
            throw new SAXException(err.toString());
        }
        parser.setEntityResolver(new EntityResolver() {

            public InputSource resolveEntity(String publicId, String SystemID) throws SAXException, IOException {
                if (SystemID.startsWith("http:/dtd")) {
                    SystemID = "http://www.ncbi.nlm.nih.gov" + SystemID.substring(5);
                }
                return new InputSource(SystemID);
            }
        });
        Document doc = parser.parse(xmlInp.getByteStream(), "http://www.ncbi.nlm.nih.gov/dtd/");
        doc.getDocumentElement().normalize();
        inputstream.close();
        return doc;
    }
