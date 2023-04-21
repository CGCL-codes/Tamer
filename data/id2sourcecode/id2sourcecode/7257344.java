    protected boolean doRemoteUnsubscribe(String remoteContextIdentity, ISubscriptionParameters parameters, IEventListener listener) {
        IChannel channel = null;
        if (remoteContextIdentity != null) {
            final DualValue<ISubscriptionParameters, IEventListener> values = new DualValue<ISubscriptionParameters, IEventListener>(parameters, listener);
            synchronized (getState()) {
                final Set<DualValue<ISubscriptionParameters, IEventListener>> remoteSubscriptions = getState().getRemoteSubscriptions().get(remoteContextIdentity);
                remoteSubscriptions.remove(values);
                if (getLog().isTraceEnabled()) {
                    getLog().trace("Remote subscriptions for remote context " + remoteContextIdentity + " are now " + CollectionUtils.toFormattedString(remoteSubscriptions));
                }
            }
            channel = getState().getChannels().get(remoteContextIdentity);
        }
        if (channel != null) {
            if (canUnsubscribe(channel, parameters, listener, true)) {
                return channel.unsubscribe(parameters);
            }
        }
        return false;
    }
