    public static void setUpITunesRss() throws Exception {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        rssHandler = new ITunesRssHandler(reader);
        reader.setContentHandler(rssHandler);
        reader.parse(new InputSource(getInputStream()));
        CHANNEL = rssHandler.getChannel();
    }
