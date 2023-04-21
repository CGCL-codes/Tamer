    @Override
    public <T extends Serializable> Boolean writeAll(String channelName, ArrayList<ChannelMessage<T>> messages) {
        ChannelServiceFactory.getChannelService().getChannel(channelName, true).writeAll(messages);
        return Boolean.TRUE;
    }
