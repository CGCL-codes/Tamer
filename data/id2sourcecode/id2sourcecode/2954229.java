    public List getChannels(String query) {
        if (cachedQueries.containsKey(query)) {
            return ((ChannelQuery) cachedQueries.get(query)).execute();
        } else {
            ChannelQuery channelQuery = new ChannelQuery(super.getDataSource(), query);
            cachedQueries.put(query, channelQuery);
            return channelQuery.execute();
        }
    }
