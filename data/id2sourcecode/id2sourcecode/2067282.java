    public void testRSS200SettingsUpdatePeriodInFeed() {
        FeedManager FM = new FeedManager(ChannelUpdatePeriod.UPDATE_HOURLY, 2);
        CacheSettings cs = new CacheSettings();
        cs.setDefaultTtl(2000);
        File inpFile = new File(getDataDir(), "mobitopia.xml");
        try {
            ChannelIF channel = FeedParser.parse(new ChannelBuilder(), inpFile);
            assertEquals((60 * 60 * 1000), cs.getTtl(channel, -1L));
            assertEquals((60 * 60 * 1000), cs.getTtl(channel, 10000L));
            assertEquals((60 * 60 * 1000), cs.getTtl(channel, (30 * 60 * 1000)));
            assertEquals((120 * 60 * 1000), cs.getTtl(channel, (120 * 60 * 1000)));
            String url = new Feed(channel).getLocation().toString();
            FM.addFeed(url);
            assertEquals((60 * 60 * 1000), cs.getTtl(FM.getFeed(url).getChannel(), -1L));
            assertEquals((60 * 60 * 1000), cs.getTtl(FM.getFeed(url).getChannel(), 10000L));
        } catch (Exception e) {
            System.err.println("testRSS200SettingsUpdatePeriodInFeed error " + e);
        }
    }
