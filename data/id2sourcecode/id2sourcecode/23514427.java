    public static Document File2Document(String archivo) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ClassLoader loader = (XMLUtilities.class).getClassLoader();
        URL urlfichero = loader.getResource(archivo);
        Document XMLDoc = factory.newDocumentBuilder().parse(new InputSource(urlfichero.openStream()));
        return XMLDoc;
    }
