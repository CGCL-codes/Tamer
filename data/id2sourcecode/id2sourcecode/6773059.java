    public BroadcastInfo(String channelName, Object broadcastObj) {
        ChannelRepository channelRepository = ChannelRepository.getInstance();
        this.channel = channelRepository.getChannel(channelName);
        this.broadcastObj = broadcastObj;
    }
