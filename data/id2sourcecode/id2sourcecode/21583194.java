    public int getChannelId(String channelName) {
        Integer id = channelNameMap.get(channelName);
        if (id == null) return -1;
        return id.intValue();
    }
