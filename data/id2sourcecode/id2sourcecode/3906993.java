    public Channel leave(final Set<? extends ClientSession> sessions) {
        getChannel().leave(sessions);
        return this;
    }
