    public void initialize(Properties props) {
        ChannelManager channelMgr = AppContext.getChannelManager();
        Channel c1 = channelMgr.createChannel(CHANNEL_1_NAME, null, Delivery.RELIABLE);
        channel1 = AppContext.getDataManager().createReference(c1);
        channelMgr.createChannel(CHANNEL_2_NAME, new HelloChannelsChannelListener(), Delivery.RELIABLE);
    }
