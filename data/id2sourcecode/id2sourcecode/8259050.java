    @Test
    @Ignore("You need to be online for this test, ignoring by default.")
    public void urlFixture() {
        try {
            final String nytimesJsonUrl = "http://prototype.nytimes.com/svc/widgets/dataservice.html?uri=http://www.nytimes.com/services/xml/rss/nyt/World.xml";
            final FixtureSource src = JSONSource.newRemoteUrl(new URL(nytimesJsonUrl));
            NyTimes nytimes = Fixjure.of(NyTimes.class).from(src).withOptions(SKIP_UNMAPPABLE).create();
            System.out.println("Successfully connected to nytimes, got version: " + nytimes.getVersion());
            assertEquals("NYT > World", nytimes.getChannel().getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            fail("You need access to nytimes.com for this test to work.");
        }
    }
