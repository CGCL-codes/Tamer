    public void invokeRpc(String remoteContextIdentity, byte[] rpcData) {
        final IChannel channel = getState().getChannels().get(remoteContextIdentity);
        if (channel == null) {
            throw new RuntimeException("There is no channel available for " + remoteContextIdentity);
        }
        channel.invokeRpc(remoteContextIdentity, rpcData);
    }
