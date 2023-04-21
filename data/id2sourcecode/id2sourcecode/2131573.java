    protected void handleJoinCompleteEvent(JoinCompleteEvent event) {
        event.getChannel().say("Connected");
        StaticData.nick = session.getNick();
        this.parent.getChatPanel().setSession(session);
        notifyObservers(event);
    }
