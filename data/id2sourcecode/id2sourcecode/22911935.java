    public List<DataChannel> getChannels(List<String> channelNames) {
        List<DataChannel> channelsRequest = new ArrayList<DataChannel>();
        for (String channelName : channelNames) {
            Channel channel = getChannel(channelName);
            if (channel != null) {
                channelsRequest.add(channel);
            }
        }
        return channelsRequest;
    }
