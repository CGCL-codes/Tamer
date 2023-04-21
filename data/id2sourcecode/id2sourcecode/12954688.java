    public HelloChannelsSessionListener(ClientSession session, ManagedReference<Channel> channel1) {
        if (session == null) {
            throw new NullPointerException("null session");
        }
        DataManager dataMgr = AppContext.getDataManager();
        sessionRef = dataMgr.createReference(session);
        sessionName = session.getName();
        ChannelManager channelMgr = AppContext.getChannelManager();
        channel1.get().join(session);
        Channel channel2 = channelMgr.getChannel(HelloChannels.CHANNEL_2_NAME);
        channel2.join(session);
    }
