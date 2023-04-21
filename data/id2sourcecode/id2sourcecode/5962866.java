    public void load() throws IOException, JDOMException, NewsfeedFactoryException {
        URL targetUrl = new URL(url);
        SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
        XMLShop.setDefaultEntityResolver(builder);
        Document document = builder.build(targetUrl.openStream());
        FeedParser parser = new FeedParser(document, targetUrl.toExternalForm());
        parser.parse();
        channel = parser.getChannel();
    }
