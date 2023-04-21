    public void initialize(Properties arg0) {
        System.out.println("sgsApp initialize..");
        ChannelManager cm = AppContext.getChannelManager();
        DataManager dm = AppContext.getDataManager();
        ChannelListener cl = new RoomChannelListener();
        Channel channel = cm.createChannel(CHANNEL_NAME, cl, Delivery.RELIABLE);
        GameRoom rm = new GameRoom(channel);
        dm.setBinding(ROOM1_NAME, rm);
    }
