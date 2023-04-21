    public ChannelIF getAggregatedChannel() {
        NewsAggregator aggregator = new NewsAggregator();
        for (int i = 0; i < readers.size(); i++) {
            RssReader rssReader = readers.get(i);
            aggregator.addChannel(rssReader.getChannel());
        }
        return aggregator.getAggregatedChannel();
    }
