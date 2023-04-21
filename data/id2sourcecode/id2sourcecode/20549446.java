    @Override
    @SuppressWarnings("unchecked")
    public T getValue() {
        return (T) ChannelServiceFactory.getChannelService().getServicePool().get(key);
    }
