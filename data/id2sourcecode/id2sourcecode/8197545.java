    public Channel getControlChannel(final NodeAgent nodeAgent) {
        return nodeAgent.getNode().getChannel(RfCavity.CAV_PHASE_SET_HANDLE);
    }
