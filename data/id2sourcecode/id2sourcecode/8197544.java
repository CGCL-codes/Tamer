    public Channel getReadbackChannel(final NodeAgent nodeAgent) {
        return nodeAgent.getNode().getChannel(RfCavity.CAV_PHASE_AVG_HANDLE);
    }
