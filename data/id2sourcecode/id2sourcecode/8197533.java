    public Channel getReadbackChannel(final NodeAgent nodeAgent) {
        return nodeAgent.getNode().getChannel(RfCavity.CAV_AMP_AVG_HANDLE);
    }
