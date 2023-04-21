    private Element readUrl(URL url) throws IOException, SAXException {
        InputStream inStream = url.openStream();
        Document doc = docBuild.parse(inStream);
        return doc.getDocumentElement();
    }
