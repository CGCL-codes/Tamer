    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        URL urlClassloaderSchemaFile = this.getClass().getResource("preferences.xsd");
        InputSource inputSource = new InputSource(urlClassloaderSchemaFile.openStream());
        return inputSource;
    }
