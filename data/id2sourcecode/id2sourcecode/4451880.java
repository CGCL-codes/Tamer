    public Channel parseBasicRss(String generatedFeed) throws Exception {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        RSSHandler rssHandler = new RSSHandler(reader);
        reader.setContentHandler(rssHandler);
        reader.parse(new InputSource(new StringReader(generatedFeed)));
        return rssHandler.getChannel();
    }
