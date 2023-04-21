    public void processUpdate(ChannelList channelList) throws ProtocolException {
        Channel channel = channelList.getChannelById(getChannelId());
        if (channel == null) {
            throw new ProtocolException("ChannelDelete for unknown channel with id " + getChannelId());
        }
        channel.removeChannel(channel);
    }
