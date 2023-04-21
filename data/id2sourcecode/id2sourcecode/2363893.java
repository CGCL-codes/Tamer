    public List<String> getSignatureResourceNames(URL url) throws IOException, ParserConfigurationException, SAXException, TransformerException, JAXBException {
        byte[] document = IOUtils.toByteArray(url.openStream());
        return getSignatureResourceNames(document);
    }
