    public static void main(String[] args) throws Exception {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        RSSHandler rssHandler = new RSSHandler(reader);
        reader.setContentHandler(rssHandler);
        reader.parse(new InputSource(getInputStream()));
        System.out.println(rssHandler.getChannel());
    }
