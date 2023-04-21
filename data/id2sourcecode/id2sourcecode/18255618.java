    public Item[] getFeed(String uri) throws FeedManagerException {
        FeedIF feed = feedManager.addFeed(uri);
        ChannelIF channel = feed.getChannel();
        Item[] items = (Item[]) channel.getItems().toArray(new Item[0]);
        return items;
    }
