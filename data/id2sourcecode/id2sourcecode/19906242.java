    public void initialize(Properties p) {
        System.out.println("initializing block world server!");
        ChannelManager cm = AppContext.getChannelManager();
        Channel c = cm.createChannel("channel 1", new BlockWorldChannelListener(), Delivery.RELIABLE);
        channel = AppContext.getDataManager().createReference(c);
    }
