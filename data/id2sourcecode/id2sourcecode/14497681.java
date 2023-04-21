    public void initialize(Properties arg0) {
        AppContext.getChannelManager().createChannel(WORLD, null, Delivery.RELIABLE);
    }
