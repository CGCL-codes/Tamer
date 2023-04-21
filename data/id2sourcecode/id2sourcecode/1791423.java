    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> ChannelServicePool<T> getServicePool() {
        LOG.fine("Creating channel service pool");
        return new ChannelServicePoolImpl<T>((ChannelImpl<T>) getChannel("__pool__", true));
    }
