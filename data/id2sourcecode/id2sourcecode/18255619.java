    public String getChannelTitle(String uri) throws FeedManagerException {
        FeedIF feed = feedManager.getFeed(uri);
        return feed.getChannel().getTitle();
    }
