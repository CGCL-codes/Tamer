    public void processUpdate(ChannelList channelList) throws ProtocolException {
        Channel channel = new Channel(getChannelId(), getName());
        channel.setTopic(getDescription());
        channel.setDescription(getDescription());
        channel.setMaxUsers(getMaxUsers());
        channel.setOrder(getOrder());
        channel.getFlags().addAll(ChannelAttributeSet.fromByte(getRawChannelAttributes()));
        Channel parentChannel = channelList.getChannelById(getChannelId());
        if (parentChannel == null) {
            channelList.addToplevelChannel(channel);
        } else {
            parentChannel.addChannel(channel);
        }
    }
