    @Override
    public void handle(ConnectionAvailableEvent event) {
        boolean destroyConnection = false;
        final IConnection connection = event.getConnection();
        synchronized (getState()) {
            final String remoteContextIdentity = connection.getRemoteContextIdentity();
            if (getState().getConnectedContexts().getCount(remoteContextIdentity) > 0) {
                final int compareTo = getState().getContext().getIdentity().compareTo(connection.getRemoteContextIdentity());
                if (compareTo > 0) {
                    destroyConnection = true;
                } else {
                    if (compareTo == 0) {
                        if (getState().getContext().getContextHashCode() > connection.getRemoteContextHashCode()) {
                            destroyConnection = true;
                        }
                    }
                }
            }
            final IConnectionParameters connectionParameters = getState().getDiscoveredContexts().get(connection.getRemoteContextIdentity());
            if (connection.isOutbound() && connectionParameters != null && !connectionParameters.isEqual(connection)) {
                destroyConnection = true;
                if (getLog().isDebugEnabled()) {
                    getLog().debug("Destroying connection " + safeToString(connection) + ", parameters have changed to " + safeToString(connectionParameters));
                }
            }
            if (!destroyConnection) {
                getState().getConnectedContexts().adjustCount(remoteContextIdentity, 1);
                getState().getChannelFactory().createChannel(connection, getState().getContext()).start();
            }
            getState().getCONNECTINGContexts().adjustCount(connection.getRemoteContextIdentity(), -1);
        }
        if (destroyConnection) {
            if (getLog().isTraceEnabled()) {
                getLog().trace("Destroying duplicate new connection " + safeToString(connection) + ", current connections are " + CollectionUtils.toFormattedString(getState().getChannels()));
            }
            connection.destroy();
        }
    }
