    @Override
    public void handle(ChannelReadyEvent event) {
        final IChannel channel = event.getChannel();
        final List<DualValue<ISubscriptionParameters, IEventListener>> list;
        synchronized (getState()) {
            final IConnectionParameters connectionParameters = getState().getDiscoveredContexts().get(channel.getRemoteContextIdentity());
            if (!validateChannelConnection(channel, connectionParameters)) {
                return;
            }
            final Map<String, IChannel> copy = CollectionFactory.newMap(getState().getChannels());
            copy.put(channel.getRemoteContextIdentity(), channel);
            getState().setChannels(copy);
            list = CollectionFactory.newList(getState().getRemoteSubscriptions().get(channel.getRemoteContextIdentity()));
        }
        if (list.size() > 0) {
            if (getLog().isInfoEnabled()) {
                getLog().info("Sending subscriptions to new channel " + safeToString(channel) + COLON + CollectionUtils.toFormattedString(list));
            }
            for (DualValue<ISubscriptionParameters, IEventListener> values : list) {
                try {
                    getState().getContext().queueEvent(new RemoteContainerSubscriptionEvent(getState().getContext(), channel.getRemoteContextIdentity(), values));
                } catch (Exception e) {
                    logException(getLog(), values, e);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        final IChannel[] connectedChannels = getState().getContext().getConnectedChannels();
        for (IChannel channel2 : connectedChannels) {
            sb.append(SystemUtils.LINE_SEPARATOR).append(SPACING_4_CHARS).append(safeToString(channel2.toString()));
        }
        if (connectedChannels.length == 0) {
            sb.append(SystemUtils.LINE_SEPARATOR).append(SPACING_4_CHARS).append("<UNCONNECTED>");
        }
        if (getLog().isInfoEnabled()) {
            getLog().info(SystemUtils.LINE_SEPARATOR + getState().getContext() + " connected to:" + sb);
        }
    }
