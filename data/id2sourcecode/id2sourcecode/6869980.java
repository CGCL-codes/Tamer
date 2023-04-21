    public ChannelId getChannelId(String id) {
        ChannelId cid = _channelIdCache.get(id);
        if (cid == null) {
            cid = new ChannelId(id);
            _channelIdCache.put(id, cid);
        }
        return cid;
    }
