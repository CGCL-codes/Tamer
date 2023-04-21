    public ChannelData getChannelData(String channelName) {
        if (!channelData.containsKey(channelName)) {
            channelData.put(channelName, new ChannelData(channelName));
        }
        return channelData.get(channelName);
    }
