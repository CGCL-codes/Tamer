    @Override
    public <T extends Serializable> Boolean writeAll(String channelName, ArrayList<ChannelMessage<T>> messages) {
        ChannelServiceFactory.getChannelService().<ChannelMessage<T>>getChannel(channelName, false).writeAll(messages);
        return Boolean.TRUE;
    }
