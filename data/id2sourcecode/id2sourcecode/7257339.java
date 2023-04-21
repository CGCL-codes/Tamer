    protected boolean doRemoteSubscribe(String remoteContextIdentity, ISubscriptionParameters parameters, IEventListener listener) {
        boolean subscribed = false;
        nullCheck(remoteContextIdentity, "No remote context identity");
        IConnectionParameters connectionParameters = null;
        DualValue<ISubscriptionParameters, IEventListener> values = new DualValue<ISubscriptionParameters, IEventListener>(parameters, listener);
        boolean connect = false;
        final IChannel channel;
        synchronized (getState()) {
            final Set<DualValue<ISubscriptionParameters, IEventListener>> remoteSubscriptions = getState().getRemoteSubscriptions().get(remoteContextIdentity);
            subscribed = !remoteSubscriptions.contains(values);
            remoteSubscriptions.add(values);
            if (getLog().isTraceEnabled()) {
                getLog().trace("Remote subscriptions for remote context " + remoteContextIdentity + " are" + CollectionUtils.toFormattedString(remoteSubscriptions));
            }
            connectionParameters = getState().getDiscoveredContexts().get(remoteContextIdentity);
            channel = getState().getChannels().get(remoteContextIdentity);
            if (channel == null) {
                connect = shouldConnect(remoteContextIdentity, connectionParameters);
            }
        }
        if (channel != null) {
            getState().getContext().queueEvent(new RemoteContainerSubscriptionEvent(getState().getContext(), remoteContextIdentity, values));
        }
        if (connect) {
            getState().getContext().getConnectionBroker().connect(connectionParameters);
        }
        return subscribed;
    }
