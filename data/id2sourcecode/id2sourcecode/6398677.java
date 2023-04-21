    private Channel getChannel(String name) {
        ChannelManager channelManager = AppContext.getChannelManager();
        try {
            channelManager.createChannel(name, this, Delivery.RELIABLE);
        } catch (Exception exception) {
        }
        return channelManager.getChannel(name);
    }
