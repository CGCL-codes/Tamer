    @Override
    public <T extends Serializable> ChannelMessage<T> get(String channelName, Long key) {
        return new ChannelMessage<T>(ChannelServiceFactory.getChannelService().<T>getServicePool().get(key));
    }
