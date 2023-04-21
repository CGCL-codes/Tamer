    public void initialize(Properties props) {
        PlayersList playersList = new PlayersList();
        DataManager dataMgr = AppContext.getDataManager();
        dataMgr.setBinding(PLAYERS_LIST_NAME, playersList);
        ChannelManager channelMgr = AppContext.getChannelManager();
        Channel c1 = channelMgr.createChannel(BASE_CHANNEL_NAME, new BaseChannelListener(), Delivery.RELIABLE);
        log.info("Mundojava Server initialiazed");
    }
