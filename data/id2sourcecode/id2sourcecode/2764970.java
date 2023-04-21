    public static Match $(URL url) throws SAXException, IOException {
        return $(url.openStream());
    }
