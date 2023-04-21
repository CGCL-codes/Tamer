    public static Document readXmlDocument(URL url, boolean validate) throws SAXException, ParserConfigurationException, java.io.IOException {
        if (url == null) {
            Debug.logWarning("[UtilXml.readXmlDocument] URL was null, doing nothing", module);
            return null;
        }
        return readXmlDocument(url.openStream(), validate, url.toString());
    }
