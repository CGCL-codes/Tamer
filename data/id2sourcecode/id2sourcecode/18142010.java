    public void processUpdate(ChannelList channelList) throws ProtocolException {
        Channel channel = channelList.getChannelById(getChannelId());
        if (channel == null) {
            throw new ProtocolException("ChannelTopicChange for unknown channel with id " + getChannelId());
        }
        channel.setTopic(getNewTopic());
    }
