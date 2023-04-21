    public Channel getChannel(String channelId, boolean create) {
        synchronized (channels) {
            ChannelImpl channel = channels.get(channelId);
            if ((channel == null) && create) {
                channel = new ChannelImpl(channelId);
                channels.put(channelId, channel);
            }
            return channel;
        }
    }
