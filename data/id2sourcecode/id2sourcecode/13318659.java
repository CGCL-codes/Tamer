    @Test
    public void testRetrieveChannelName() throws JCouplingException {
        String channelName = "TestChannel1";
        logger.debug("Retrieving the channel ...");
        Channel channel = dMapper.retrieveChannel(channelName);
        logger.debug("ChannelID: " + channel.getChannelID());
        logger.debug("ChannelName: " + channel.getChannelName());
        logger.debug("MiddlewareAdapterID: " + channel.getMiddlewareAdapterID());
        logger.debug("IsTimeDecoupled: " + channel.getIsTimeDecoupled());
        logger.debug("IsWsdlBacked: " + channel.getIsWSDLBacked());
        logger.debug("MessageSchemaIn: " + channel.getMsgSchemaIn());
        logger.debug("MessageSchemaOut: " + channel.getMsgSchemaOut());
        logger.debug("SupportsInvoke: " + channel.supportsInvoke());
        logger.debug("SupportsInbound: " + channel.supportsInbound());
        logger.debug("Done");
    }
