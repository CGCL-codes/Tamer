    public void testRSS100SettingsUpdatePeriodInFeed() {
        FeedManager FM = new FeedManager(ChannelUpdatePeriod.UPDATE_HOURLY, 2);
        CacheSettings cs = new CacheSettings();
        cs.setDefaultTtl(2000);
        File inpFile = new File(getDataDir(), "slashdot-010604.rdf");
        try {
            ChannelIF channel = FeedParser.parse(new ChannelBuilder(), inpFile);
            assertEquals(3600000, cs.getTtl(channel, -1L));
            assertEquals(3600000, cs.getTtl(channel, 10000L));
            assertEquals(3600005, cs.getTtl(channel, 3600005));
            String url = new Feed(channel).getLocation().toString();
            FM.addFeed(url);
            assertEquals(3600000, cs.getTtl(FM.getFeed(url).getChannel(), -1L));
            assertEquals(3600000, cs.getTtl(FM.getFeed(url).getChannel(), 10000L));
        } catch (Exception e) {
            System.err.println("testRSS100SettingsUpdatePeriodInFeed error " + e);
        }
    }
