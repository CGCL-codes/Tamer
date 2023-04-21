    public Channel getControlChannel(final NodeAgent nodeAgent) {
        return nodeAgent.getNode().getChannel(RfCavity.CAV_AMP_SET_HANDLE);
    }
