    public Document getSignatureDocument(URL url, String signatureResourceName) throws IOException, ParserConfigurationException, SAXException {
        return getSignatureDocument(url.openStream(), signatureResourceName);
    }
