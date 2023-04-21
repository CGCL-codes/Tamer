    public void deregisterListener(String channelName, ChannelProgram channelProgram) {
        Channel channel = getChannel(channelName);
        channel.removeListener(this, channelProgram);
    }
