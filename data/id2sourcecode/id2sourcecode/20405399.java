    @Override
    public void onJoin(JoinEvent<CubeIRC> event) throws Exception {
        MessageQueue.addQueue(MessageQueueEnum.CHANNEL_JOIN, new ChannelResponse(event.getChannel(), event.getUser(), event.getTimestamp()));
        DebuggerQueue.addDebug(this, Level.INFO, "User %s join channel %s", event.getUser().getNick(), event.getChannel().getName());
        super.onJoin(event);
    }
