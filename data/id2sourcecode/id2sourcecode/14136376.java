    @Override
    public void handle(Event event) {
        if (logger.isDebugEnabled()) logger.debug("TOP session received event " + event + " " + (event.getDir() == Direction.DOWN ? "Down" : "Up") + " from channel " + event.getChannel().getChannelID());
        if (event instanceof JGCSSendableEvent) handleSendableEvent((JGCSSendableEvent) event); else if (event instanceof MessageSender) handleMessageSender((MessageSender) event); else if (event instanceof ChannelInit) handleChannelInit((ChannelInit) event); else if (event instanceof RegisterSocketEvent) handleRegisterSocket((RegisterSocketEvent) event); else if (event instanceof MulticastInitEvent) handleMulticastInit((MulticastInitEvent) event); else if (event instanceof ChannelClose) handleChannelClose((ChannelClose) event); else super.handle(event);
    }
