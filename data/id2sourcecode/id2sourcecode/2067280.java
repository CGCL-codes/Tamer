    public void testRSS100SettingsNoUpdatePeriodInFeed() {
        FeedManager FM = new FeedManager(ChannelUpdatePeriod.UPDATE_HOURLY, 2);
        CacheSettings cs = new CacheSettings();
        cs.setDefaultTtl(2000);
        File inpFile = new File(getDataDir(), "bloggy.rdf");
        try {
            ChannelIF channel = FeedParser.parse(new ChannelBuilder(), inpFile);
            assertEquals(2000, cs.getTtl(channel, -1L));
            assertEquals(2000L, cs.getTtl(channel, 10000L));
            assertEquals(360000L, cs.getTtl(channel, 360000L));
            String url = new Feed(channel).getLocation().toString();
            FM.addFeed(url);
            assertEquals(2000, cs.getTtl(FM.getFeed(url).getChannel(), -1L));
            assertEquals(2000L, cs.getTtl(FM.getFeed(url).getChannel(), 10000L));
            assertEquals(360000L, cs.getTtl(FM.getFeed(url).getChannel(), 360000L));
        } catch (Exception e) {
            System.err.println("testRSS100SettingsNoUpdatePeriodInFeed error " + e);
            e.printStackTrace();
        }
    }
