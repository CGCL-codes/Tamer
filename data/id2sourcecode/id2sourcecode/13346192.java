    public static Document file2Document(String path) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ClassLoader loader = (UtilsXML.class).getClassLoader();
        URL urlFile = loader.getResource(path);
        Document xmlDoc = factory.newDocumentBuilder().parse(new InputSource(urlFile.openStream()));
        return xmlDoc;
    }
