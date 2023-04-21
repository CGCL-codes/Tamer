    @Override
    public void onNotice(NoticeEvent<CubeIRC> event) throws Exception {
        DebuggerQueue.addDebug(this, Level.INFO, "NOTICE: User %s Message %s", event.getUser().getNick(), event.getMessage());
        if (event.getChannel() != null) MessageQueue.addQueue(MessageQueueEnum.CHANNEL_NOTICE, new GenericChannelResponse(event.getChannel(), event.getUser(), event.getNotice())); else MessageQueue.addQueue(MessageQueueEnum.IRC_NOTICE, new GenericUserResponse(event.getUser(), event.getNotice()));
        super.onNotice(event);
    }
