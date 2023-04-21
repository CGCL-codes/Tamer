    public SyndFeed getFeed(SettingsBean bean, String selectedFeed) throws IOException, FeedException {
        SyndFeed feed = null;
        FeedElement feedElement = (FeedElement) feeds.get(selectedFeed);
        if (feedElement != null && !feedElement.isExpired()) {
            feed = feedElement.getFeed();
        } else {
            URL feedUrl = new URL(selectedFeed);
            URLConnection urlc = feedUrl.openConnection();
            urlc.connect();
            SyndFeedInput input = new SyndFeedInput();
            InputSource src = new InputSource(urlc.getInputStream());
            feed = input.build(src);
            int timeout = bean.getCacheTimeout();
            if (timeout != 0) {
                putFeed(selectedFeed, feed, timeout);
            }
        }
        return feed;
    }
