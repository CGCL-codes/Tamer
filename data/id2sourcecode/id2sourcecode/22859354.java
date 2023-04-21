    @Override
    public void onMessage(MessageEvent<CubeIRC> event) throws Exception {
        DebuggerQueue.addDebug(this, Level.DEBUG, "Channel %s message from %s: %s", event.getChannel().getName(), event.getUser().getNick(), event.getMessage());
        MessageQueue.addQueue(MessageQueueEnum.MSG_CHANNEL_IN, new ChannelMessageResponse(event.getChannel(), event.getUser(), event.getMessage()));
        super.onMessage(event);
    }
