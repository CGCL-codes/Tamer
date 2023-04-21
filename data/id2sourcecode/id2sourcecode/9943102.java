    public void registerBroadcast(String channelName, ChannelProgram channelProgram) {
        Channel channel = getChannel(channelName);
        channel.addBroadcaster(this, channelProgram);
    }
