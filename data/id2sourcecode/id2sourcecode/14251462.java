    public static void setUpBasicRss() throws Exception {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        rssHandler = new RSSHandler(reader);
        reader.setContentHandler(rssHandler);
        reader.parse(new InputSource(getInputStream()));
        CHANNEL = rssHandler.getChannel();
    }
