    public void addFilter(String channels, DataFilter filter) {
        synchronized (this) {
            ChannelImpl channel = (ChannelImpl) getChannel(channels, true);
            channel.addDataFilter(filter);
        }
    }
