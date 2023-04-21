    @Override
    public <T extends Serializable> ChannelMessage<T> put(String channelName, ChannelMessage<T> value) {
        return ChannelServiceFactory.getChannelService().<T>getServicePool().getMessage(value.getValue());
    }
