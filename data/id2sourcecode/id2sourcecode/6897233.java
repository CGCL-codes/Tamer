    public static XmlDocument read(URL url) throws SAXException, IOException {
        InputStream input = url.openStream();
        Document document = XmlDocument.builder.parse(input);
        input.close();
        return new XmlDocument(document);
    }
