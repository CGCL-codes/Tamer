    public ChannelImpl getChild(ChannelId id) {
        String next = id.getSegment(_id.depth());
        if (next == null) return null;
        ChannelImpl channel = _children.get(next);
        if (channel == null || channel.getChannelId().depth() == id.depth()) {
            return channel;
        }
        return channel.getChild(id);
    }
