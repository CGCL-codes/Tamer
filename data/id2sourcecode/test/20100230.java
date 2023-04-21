    public Document parse(URL url) throws XMLPlatformException {
        try {
            InputStream inputStream = url.openStream();
            return parse(inputStream);
        } catch (IOException e) {
            throw XMLPlatformException.xmlPlatformParseException(e);
        }
    }
