    public void diff(Channels channelsBase, Channels channelsChanged) {
        List<Channel> channels = channelsBase.getChannels();
        for (Channel channel : channels) {
            Channel channel2 = channelsChanged.getChannel(channel.getId());
            if (channel2 == null) {
                deleteFromBase.getChannels().addChannel(channel);
            } else {
                diff(channel, channel2);
            }
        }
        for (Channel channel : channelsChanged.getChannels()) {
            Channel channel2 = channelsBase.getChannel(channel.getId());
            if (channel2 == null) {
                addToBase.getChannels().addChannel(channel);
            }
        }
    }
