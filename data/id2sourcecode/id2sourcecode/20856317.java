    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> ChannelServicePool<T> getServicePool() {
        return new ChannelServicePoolImpl<T>((ChannelImpl<T>) getChannel("__pool__", true));
    }
