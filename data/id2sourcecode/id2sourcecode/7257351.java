    public void retransmitAll(String contextIdentity) {
        if (getLog().isDebugEnabled()) {
            getLog().debug("requestAll for context=" + contextIdentity);
        }
        final IChannel channel = getChannel(contextIdentity);
        if (channel != null) {
            channel.retransmitAll();
        }
    }
