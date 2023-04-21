    public Channel getChannel(String id, boolean create) {
        synchronized (this) {
            ChannelImpl channel = getChannel(id);
            if (channel == null && create) {
                channel = new ChannelImpl(id, this);
                _root.addChild(channel);
                if (isLogInfo()) logInfo("newChannel: " + channel);
            }
            return channel;
        }
    }
