    private String getChannelTitle(String url) {
        ChannelBuilder builder = new ChannelBuilder();
        try {
            builder.beginTransaction();
            ChannelIF parsedChannel = FeedParser.parse(builder, url);
            builder.endTransaction();
            return parsedChannel.getTitle();
        } catch (Exception ex) {
        }
        return null;
    }
