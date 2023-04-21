    @Override
    public void fetch(URL url, ResponseHandler handler) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.newSAXParser().parse(url.openStream(), handler);
        } catch (Exception e) {
            throw new JiggException(e);
        }
    }
