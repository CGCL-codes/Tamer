    public static void testBrowseChannels(KyteSession session) {
        ChannelService channelService = ServiceFactory.getChannelService();
        Page<Channel> channels = channelService.browseChannels(session, "LATEST", 0, 5, null);
        LOGGER.info("channels: " + channels);
        Channel channel = channels.getItems().get(0);
        Page<Show> shows = channel.fetchShows(session, 0, 10);
        LOGGER.info("channel: " + channel.getUri() + " has " + shows.getTotalSize() + " shows.");
    }
