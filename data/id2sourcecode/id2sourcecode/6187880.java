    private RssFeed getRssFeed() throws Exception {
        RssFeed rss = new RssFeed();
        rss.setChannel(this.getChannel());
        rss.setImage(this.getImage());
        rss.setItems(this.getItems());
        return rss;
    }
