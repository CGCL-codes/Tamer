    @Override
    public void handle(ChannelReadyEvent event) {
        final String remoteContextIdentity = event.getChannel().getRemoteContextIdentity();
        final List<QuadValue<String, IField[], IRpcResultHandler, IRpcMarker>> pendingRpcs;
        final Set<String> connectedContexts = getState().getConnectedContexts();
        synchronized (connectedContexts) {
            connectedContexts.add(remoteContextIdentity);
            pendingRpcs = getState().getPendingRpcInvocations().remove(remoteContextIdentity);
        }
        if (pendingRpcs == null) {
            return;
        }
        if (getLog().isDebugEnabled()) {
            getLog().debug("Invoking " + pendingRpcs.size() + " RPCs for " + remoteContextIdentity);
        }
        for (QuadValue<String, IField[], IRpcResultHandler, IRpcMarker> rpc : pendingRpcs) {
            final DualValue<String, IRpcDefinition> keyAndDefinition = getOperations().getRegistryKeyAndDefinition(remoteContextIdentity, rpc.getFirst(), rpc.getSecond());
            getOperations().invoke(remoteContextIdentity, keyAndDefinition.getFirst(), keyAndDefinition.getSecond(), rpc.getSecond(), rpc.getThird(), rpc.getFourth());
        }
    }
