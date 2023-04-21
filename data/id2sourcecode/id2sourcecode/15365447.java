    public void XXXtestCreate() throws Exception {
        ChannelRegistry reg = new ChannelRegistry(new ChannelBuilder());
        File inpFile = new File(getDataDir(), "xmlhack-0.91.xml");
        ChannelIF chA = reg.addChannel(inpFile.toURL(), 2, true);
        Date dateA = chA.getLastUpdated();
        assertNull("channel shouldn't be parsed now", dateA);
        inpFile = new File(getDataDir(), "pro-linux.rdf");
        ChannelIF chB = reg.addChannel(inpFile.toURL(), 2, true);
        inpFile = new File(getDataDir(), "snipsnap-org.rss");
        ChannelIF chC = reg.addChannel(inpFile.toURL(), 2, true);
        assertEquals("channel exists", 3, reg.getChannels().size());
        assertTrue("channel A", reg.getChannels().contains(chA));
        assertTrue("channel B", reg.getChannels().contains(chB));
        assertTrue("channel C", reg.getChannels().contains(chC));
        logger.info("starting to sleep ...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.warn("Interrupted waiting thread");
        }
        logger.info("... stopped sleeping");
        assertTrue("channel A active", reg.isActiveChannel(chA));
        assertTrue("channel B active", reg.isActiveChannel(chB));
        assertTrue("channel C active", reg.isActiveChannel(chC));
        assertNotNull("channel should have been updated in the meantime", chA.getLastUpdated());
    }
