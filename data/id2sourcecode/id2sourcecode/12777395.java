    public ChannelBuilder(String link) throws ChannelBuilderException {
        this.link = link;
        openDocument(false);
        if (document == null) return;
        FeedParser parser = new FeedParser(document, link);
        parser.parse();
        channel = parser.getChannel();
    }
