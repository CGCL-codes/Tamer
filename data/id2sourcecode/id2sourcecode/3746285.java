    public Component parse(URL url) throws SAXException, IOException, ParserConfigurationException {
        return parse(url.openStream());
    }
