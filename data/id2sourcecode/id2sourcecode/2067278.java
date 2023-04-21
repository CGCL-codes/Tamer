    public void testRSS091SettingsDefault() {
        FeedManager FM = new FeedManager(ChannelUpdatePeriod.UPDATE_HOURLY, 2);
        CacheSettings cs = new CacheSettings();
        cs.setDefaultTtl(2000);
        File inpFile = new File(getDataDir(), "xmlhack-0.91.xml");
        try {
            ChannelIF channel = FeedParser.parse(new ChannelBuilder(), inpFile);
            assertEquals(2000, cs.getTtl(channel, -1L));
            String url = new Feed(channel).getLocation().toString();
            FM.addFeed(url);
            assertEquals(2000, cs.getTtl(FM.getFeed(url).getChannel(), -1L));
        } catch (Exception e) {
            System.err.println("testRSS091SettingsDefault error " + e);
            e.printStackTrace();
        }
    }
