    @Override
    public void onMode(ModeEvent<CubeIRC> event) throws Exception {
        MessageQueue.addQueue(MessageQueueEnum.IRC_MODE, new ChannelMessageResponse(event.getChannel(), event.getUser(), event.getMode()));
        super.onMode(event);
    }
