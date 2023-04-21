    @Override
    public T getValue() {
        return (T) ChannelServiceFactory.getChannelService().<T>getServicePool().get(key);
    }
