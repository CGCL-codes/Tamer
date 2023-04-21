    protected synchronized MessageDispatcher openChannel(String type_id) throws ChannelException, ChannelClosedException {
        String group_id = type_id;
        if (dispatchers.get(group_id) != null) return (MessageDispatcher) dispatchers.get(group_id);
        Channel channel = new JChannel(props);
        channel.setOpt(Channel.LOCAL, new Boolean(false));
        MessageDispatcher dispatcher = new MessageDispatcher(channel, null, this, this);
        channel.connect(group_id);
        Debug.output(1, "JavaGroupsAdaptor: Channel: " + channel.getChannelName() + " Address: " + channel.getLocalAddress());
        channels.put(group_id, channel);
        dispatchers.put(group_id, dispatcher);
        return dispatcher;
    }
