    public AbstractIRCChannel addChannel(String name) {
        if (name == null) return null;
        AbstractIRCChannel channel = getChannelImpl(name);
        if (channel == null) return null;
        channels.add(channel);
        fireChannelCreatedEvent(channel);
        return channel;
    }
