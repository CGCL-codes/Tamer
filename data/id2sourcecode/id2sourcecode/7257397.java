    @Override
    public void handle(ConnectionDestroyedEvent event) {
        final IConnectionDiscoverer connectionDiscoverer = getState().getContext().getConnectionDiscoverer();
        if (connectionDiscoverer != null) {
            connectionDiscoverer.connectionDestroyed(event.getRemoteContextIdentity());
        }
        IChannel channel = null;
        final String remoteContextIdentity = event.getRemoteContextIdentity();
        synchronized (getState()) {
            final Map<String, IChannel> copy = CollectionFactory.newMap(getState().getChannels());
            channel = copy.remove(remoteContextIdentity);
            getState().setChannels(copy);
            getState().getConnectedContexts().adjustCount(remoteContextIdentity, -1);
        }
        if (channel != null) {
            if (getLog().isInfoEnabled()) {
                getLog().info("Destroying " + safeToString(channel));
            }
            channel.destroy();
        }
    }
