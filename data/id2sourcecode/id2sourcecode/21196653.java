    public Channel(Rss feed) {
        channelName = feed.getChannel().getTitle();
        int channelNameEnd = channelName.indexOf(" - ");
        channelName = channelName.substring(0, channelNameEnd);
        channelShows = new Vector<Show>();
        Vector<Item> items = feed.getChannel().getItems();
        if (items != null && items.size() > 0) {
            int itemCount = items.size();
            for (int i = 0; i < itemCount; i++) {
                channelShows.add(new Show(items.elementAt(i)));
            }
        }
    }
