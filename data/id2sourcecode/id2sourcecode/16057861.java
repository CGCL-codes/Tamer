    @Override
    protected void handleJoinCompleteEvent(JoinCompleteEvent event) {
        event.getChannel().say("Hello from BaseListenerExample");
    }
