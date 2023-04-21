    protected void setUp() throws Exception {
        super.setUp();
        TestUtils.loadData();
        broadcastDao = DAOFactory.getBroadcastDAO(TestUtils.getHibernateSingletonSessionFactory());
        channelDao = DAOFactory.getChannelDAO(TestUtils.getHibernateSingletonSessionFactory());
    }
