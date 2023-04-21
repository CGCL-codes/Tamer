    public void retransmitAllToAll() {
        if (getLog().isDebugEnabled()) {
            getLog().debug("requestAllToAll");
        }
        for (IChannel channel : getState().getChannels().values()) {
            try {
                channel.retransmitAll();
            } catch (Exception e) {
                logException(getLog(), channel, e);
            }
        }
    }
