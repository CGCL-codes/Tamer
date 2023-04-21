    public ChannelImpl getChannel(String id) {
        ChannelId cid = getChannelId(id);
        if (cid.depth() == 0) return null;
        return _root.getChild(cid);
    }
