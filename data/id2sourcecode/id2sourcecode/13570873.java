    public FeedIF addFeed(String feedUri) throws FeedManagerException {
        if (!hasFeed(feedUri)) {
            FeedManagerEntry fme = new FeedManagerEntry(feedUri, getChannelBuilder(), defaultUpdatePeriod, defaultUpdateFrequency);
            feeds.put(feedUri, fme);
            refreshDaemon.addFeed(fme);
        }
        return getFeed(feedUri);
    }
