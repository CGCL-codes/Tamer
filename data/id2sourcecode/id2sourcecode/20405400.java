    @Override
    public void onPart(PartEvent<CubeIRC> event) throws Exception {
        MessageQueue.addQueue(MessageQueueEnum.CHANNEL_PART, new ChannelResponse(event.getChannel(), event.getUser(), event.getTimestamp()));
        DebuggerQueue.addDebug(this, Level.INFO, "User %s part channel %s", event.getUser().getNick(), event.getChannel().getName());
        super.onPart(event);
    }
