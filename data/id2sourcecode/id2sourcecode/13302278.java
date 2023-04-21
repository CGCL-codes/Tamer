    public void parseURL(String spec) throws IOException, SAXException {
        URL url;
        try {
            url = new URL(spec);
        } catch (MalformedURLException mue) {
            logger.error("invalid-url", mue);
            throw new ExtendedSAXException(mue);
        }
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        parser.parse(new InputSource(connection.getInputStream()));
    }
