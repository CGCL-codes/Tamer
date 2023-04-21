    @Test
    public void testAddConstructor() throws JCouplingException {
        logger.debug("Adding the channel ...");
        Channel c1 = new Channel("TestChannel4", 1);
        logger.debug("Channel with ID " + c1.getChannelID() + " added!");
        logger.debug("Done");
    }
