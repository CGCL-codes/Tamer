    public Channel setUpBasicRss() throws Exception {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        RSSHandler rssHandler = new RSSHandler(reader);
        reader.setContentHandler(rssHandler);
        reader.parse(new InputSource(getInputStream()));
        return rssHandler.getChannel();
    }
