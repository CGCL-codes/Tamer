    private Channel retrieveChannel(InputStream inputStream) throws SAXException, IOException {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        RSSHandler rssHandler = new RSSHandler(reader);
        reader.setContentHandler(rssHandler);
        reader.parse(new InputSource(inputStream));
        return rssHandler.getChannel();
    }
