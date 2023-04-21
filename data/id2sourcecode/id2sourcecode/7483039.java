    private Document getRssDocument() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        URL url = new URL(filesRss);
        return builder.parse(url.openStream());
    }
