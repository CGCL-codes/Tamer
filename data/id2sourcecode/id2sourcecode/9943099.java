    public void deregisterBroadcast(String channelName, ChannelProgram channelProgram) {
        Channel channel = getChannel(channelName);
        channel.removeBroadcaster(this, channelProgram);
    }
