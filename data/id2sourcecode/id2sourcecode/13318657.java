    @Test
    public void testRetrieveAllChannels() throws JCouplingException {
        logger.debug("Retrieving all the channels ...");
        ArrayList<Channel> channels = dMapper.retrieveAllChannels();
        Iterator<Channel> ChannelIterator = channels.iterator();
        Channel channel = null;
        while (ChannelIterator.hasNext()) {
            channel = ChannelIterator.next();
            logger.debug("|=============================|");
            logger.debug("ChannelID: " + channel.getChannelID());
            logger.debug("ChannelName: " + channel.getChannelName());
            logger.debug("MiddlewareAdapterID: " + channel.getMiddlewareAdapterID());
            logger.debug("IsTimeDecoupled: " + channel.getIsTimeDecoupled());
        }
        logger.debug("Done");
    }
