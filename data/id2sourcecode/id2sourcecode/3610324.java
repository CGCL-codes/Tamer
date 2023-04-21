    @Override
    public void initialize(Properties props) {
        AppContext.getChannelManager().createChannel(LOBBY_CHANNEL, null, Delivery.RELIABLE);
        AppContext.getDataManager().setBinding(LOBBY, new Lobby());
    }
