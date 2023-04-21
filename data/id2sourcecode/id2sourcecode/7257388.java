    @Override
    public void handle(ContextNotAvailableEvent event) {
        IChannel channel;
        synchronized (getState()) {
            getState().getDiscoveredContexts().remove(event.getRemoteContextIdentity());
            channel = getState().getChannels().get(event.getRemoteContextIdentity());
        }
        if (channel != null) {
            final IConnection connection = channel.getConnection();
            if (connection != null) {
                connection.destroy();
            }
        }
    }
