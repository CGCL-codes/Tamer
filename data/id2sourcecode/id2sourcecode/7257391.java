    @Override
    public void handle(ContextDiscoveredEvent event) {
        if (!getState().getContext().isActive()) {
            if (getLog().isInfoEnabled()) {
                getLog().info("Context is not active, ignoring " + safeToString(event));
            }
            return;
        }
        final IConnectionParameters connectionParameters = event.getConnectionParameters();
        final String remoteContextIdentity = connectionParameters.getRemoteContextIdentity();
        nullCheck(remoteContextIdentity, "No remote context identity");
        boolean connect = false;
        synchronized (getState()) {
            connect = getState().getRemoteSubscriptions().get(remoteContextIdentity).size() > 0;
            if (connect) {
                if (getLog().isDebugEnabled()) {
                    getLog().debug("Connect pending because there are remote subscriptions to " + remoteContextIdentity + CollectionUtils.toFormattedString(getState().getRemoteSubscriptions().get(remoteContextIdentity)));
                }
                connect = getState().getConnectedContexts().getCount(remoteContextIdentity) == 0;
                if (getLog().isDebugEnabled()) {
                    getLog().debug("Connected contexts are " + safeToString(getState().getConnectedContexts()));
                }
                if (connect) {
                    if (getLog().isDebugEnabled()) {
                        getLog().debug("Connect still pending because there is no connection, connecting contexts are " + safeToString(getState().getCONNECTINGContexts()));
                    }
                    connect = getState().getCONNECTINGContexts().getCount(remoteContextIdentity) == 0;
                    if (connect) {
                        getState().getCONNECTINGContexts().adjustCount(remoteContextIdentity, 1);
                    } else {
                        if (getLog().isDebugEnabled()) {
                            getLog().debug("Already trying to connect to " + safeToString(event.getConnectionParameters()));
                        }
                    }
                } else {
                    if (getLog().isDebugEnabled()) {
                        getLog().debug("Already connected to " + safeToString(event.getConnectionParameters()) + ", current connections are: " + safeToString(getState().getConnectedContexts()));
                    }
                }
            }
            getState().getDiscoveredContexts().put(remoteContextIdentity, event.getConnectionParameters());
            if (getLog().isInfoEnabled()) {
                getLog().info("Discovered contexts are: " + CollectionUtils.toFormattedString(getState().getDiscoveredContexts()));
            }
        }
        final IChannel channel = getState().getChannels().get(remoteContextIdentity);
        if (channel != null) {
            validateChannelConnection(channel, event.getConnectionParameters());
        }
        if (connect) {
            try {
                getState().getContext().getConnectionBroker().connect(event.getConnectionParameters());
            } catch (RuntimeException e) {
                synchronized (getState()) {
                    getState().getCONNECTINGContexts().adjustCount(remoteContextIdentity, -1);
                }
                throw e;
            }
        } else {
            if (getLog().isDebugEnabled()) {
                getLog().debug("Not connecting to (already connected or not required) " + safeToString(event.getConnectionParameters()));
            }
        }
    }
