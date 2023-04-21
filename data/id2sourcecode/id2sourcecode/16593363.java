    public static Element loadDocument(String location) {
        Document doc = null;
        try {
            URL url = new URL(location);
            InputSource xmlInp = new InputSource(url.openStream());
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
            doc = parser.parse(xmlInp);
            Element root = doc.getDocumentElement();
            root.normalize();
            return root;
        } catch (SAXParseException err) {
            logger.error("URLMappingsXmlDAO ** Parsing error, line {}, uri {}.", Integer.valueOf(err.getLineNumber()), err.getSystemId());
            logger.error("URLMappingsXmlDAO error: ", err);
        } catch (Exception pce) {
            logger.error("URLMappingsXmlDAO error: ", pce);
        }
        return null;
    }
