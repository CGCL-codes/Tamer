    public void removeFilter(String channels, DataFilter filter) {
        synchronized (this) {
            ChannelImpl channel = (ChannelImpl) getChannel(channels, false);
            if (channel != null) channel.removeDataFilter(filter);
        }
    }
