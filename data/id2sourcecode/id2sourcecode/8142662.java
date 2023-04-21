    public List verifyRss(String feed) throws Exception {
        RssParser parser = new RssParserImpl();
        Rss rss = parser.parse(new URL(feed));
        Collection items = rss.getChannel().getItems();
        List rssItem = new ArrayList();
        if (items != null && !items.isEmpty()) {
            for (Iterator i = items.iterator(); i.hasNext(); ) {
                Item item = (Item) i.next();
                rssItem.add(item.getTitle());
                rssItem.add(item.getLink());
                rssItem.add(item.getDescription());
            }
        }
        return rssItem;
    }
