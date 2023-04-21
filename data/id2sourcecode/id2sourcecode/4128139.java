    public static XMLLayout getLayout(URL url) throws SAXException, IOException {
        return getLayout(url.openStream());
    }
