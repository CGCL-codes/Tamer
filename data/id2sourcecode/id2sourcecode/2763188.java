    public static void writeFeed(final String path, final FeedItem item) throws JAXBException, IOException {
        RSS feed = null;
        if (new File(path).exists()) feed = RSS.loadRSS(path); else feed = new RSS();
        FeedChannel channel = feed.getChannel();
        if (channel == null) {
            channel = new FeedChannel(FEED_TITLE, path, FEED_DESCRIPTION);
            feed.setChannel(channel);
        }
        channel.addItem(item);
        feed.saveRSS(path);
    }
