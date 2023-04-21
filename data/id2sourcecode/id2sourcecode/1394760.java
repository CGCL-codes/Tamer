    public void testStoreChannel() throws Exception {
        assertEquals(1, channelDao.getChannelCount());
        RSSChannel channel = new RSSChannel();
        String feed = "http://www.theserverside.com/rss/theserverside-1.0.rdf";
        channel.setFeed(feed);
        channelDao.saveChannel(channel);
        assertEquals(2, channelDao.getChannelCount());
        channel = channelDao.findChannelByFeed(feed);
        assertNotNull(channel);
    }
