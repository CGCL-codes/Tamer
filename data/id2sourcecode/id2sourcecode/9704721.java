    public ChannelIF getChannel(String url) throws MalformedURLException, IOException, UnsupportedFormatException, ParseException {
        if (channels.containsKey(url)) {
            ChannelIF channel = channels.get(url);
            if (new java.util.Date().getTime() - channel.getLastUpdated().getTime() < 60 * 60 * 1000) return channel;
        }
        try {
            ChannelIF channel = FeedParser.parse(new ChannelBuilder(), new URL(url));
            channels.put(url, channel);
            return channel;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
