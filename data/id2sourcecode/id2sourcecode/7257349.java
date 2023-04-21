    public void requestRetransmitAll(String contextIdentity) {
        if (getLog().isDebugEnabled()) {
            getLog().debug("requestRetransmitAll for context=" + contextIdentity);
        }
        final IChannel channel = getChannel(contextIdentity);
        if (channel != null) {
            channel.requestRetransmitAll();
        }
    }
