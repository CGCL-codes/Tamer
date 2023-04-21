    @Override
    public void handle(RemoteContainerSubscriptionEvent event) {
        final IChannel channel = getState().getChannels().get(event.getRemoteContextIdentity());
        if (channel != null) {
            channel.subscribe(event.getParameters());
            channel.addListener(event.getParameters(), event.getListener());
        }
    }
