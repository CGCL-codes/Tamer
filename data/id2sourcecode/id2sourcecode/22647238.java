    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        URL url = getURL(systemId);
        if (url == null) {
            return null;
        } else {
            return new InputSource(url.openStream());
        }
    }
