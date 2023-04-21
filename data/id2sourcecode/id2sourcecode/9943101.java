    public void registerListener(String channelName, ChannelProgram channelProgram) {
        Channel channel = getChannel(channelName);
        channel.addListener(this, channelProgram);
    }
