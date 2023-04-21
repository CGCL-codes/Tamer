    public FeedIF addFeed(String feedUri, int wantedTtlmins) throws FeedManagerException {
        if (!hasFeed(feedUri)) {
            FeedManagerEntry FMEntry = new FeedManagerEntry(feedUri, getChannelBuilder(), defaultUpdatePeriod, defaultUpdateFrequency);
            if (wantedTtlmins > 0) {
                FMEntry.setWantedTtl(wantedTtlmins * (60 * 1000));
            }
            feeds.put(feedUri, FMEntry);
            refreshDaemon.addFeed(FMEntry);
        } else {
        }
        return getFeed(feedUri);
    }
