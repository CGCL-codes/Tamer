    public Document getDocument(URL url) throws SAXException, IOException {
        InputStream ins = url.openStream();
        return getDocument(ins);
    }
