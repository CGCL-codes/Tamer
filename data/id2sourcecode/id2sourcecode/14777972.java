    @Override
    public final void run() throws Exception {
        action();
        ChannelManager channelManager = AppContext.getChannelManager();
        Channel channel = channelManager.getChannel("position");
        MovementMessage message = new MovementMessage(id, this, speed);
        channel.send(message.toBytes().array());
    }
