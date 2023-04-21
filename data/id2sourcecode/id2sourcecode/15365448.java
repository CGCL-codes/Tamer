    public void testParseProblem() throws Exception {
        ChannelRegistry reg = new ChannelRegistry(new ChannelBuilder());
        reg.setAcceptNrOfErrors(1);
        File inpFile = new File(getDataDir(), "xmlhack-0.91.xml");
        File chFile = new File(getOutputDir(), "xmlhack-0.91.xml");
        synchronized (chFile) {
            FileUtils.copyFile(inpFile, chFile);
        }
        ChannelIF chA = reg.addChannel(chFile.toURL(), 2, true);
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            logger.warn("Interrupted waiting thread");
        }
        assertEquals("channel exists", 1, reg.getChannels().size());
        assertTrue("channel A", reg.getChannels().contains(chA));
        UpdateChannelInfo info = reg.getUpdateInfo(chA);
        assertTrue("channel A active", reg.isActiveChannel(chA));
        assertNull("no exception", info.getLastException());
        assertEquals("NrProblems", 0, info.getNrProblemsOccurred());
        logger.info("deleting channel file");
        synchronized (chFile) {
            chFile.delete();
        }
        logger.info("starting to sleep ...");
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            logger.warn("Interrupted waiting thread");
        }
        logger.info("... stopped sleeping");
        info = reg.getUpdateInfo(chA);
        assertTrue("channel A should be deactive", !reg.isActiveChannel(chA));
        logger.debug("exception: " + info.getLastException());
        assertNotNull("Exception", info.getLastException());
        assertEquals("NrProblems", 1, info.getNrProblemsOccurred());
    }
