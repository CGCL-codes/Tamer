    public void processUpdate(ChannelList channelList) throws ProtocolException {
        Channel channel = channelList.getChannelById(getChannelId());
        if (channel == null) {
            throw new ProtocolException("ChannelDescriptionChange for unknown channel with id " + getChannelId());
        }
        channel.setTopic(getNewDescription());
    }
