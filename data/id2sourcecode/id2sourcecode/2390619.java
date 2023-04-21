    public static Document getDocument(URL url) throws IOException, SAXException {
        return getDocument(url.openConnection());
    }
