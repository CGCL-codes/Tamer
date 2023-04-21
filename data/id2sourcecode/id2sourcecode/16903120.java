    public Channel getReadbackChannel(final NodeAgent nodeAgent) {
        return nodeAgent.getNode().getChannel(Electromagnet.FIELD_RB_HANDLE);
    }
