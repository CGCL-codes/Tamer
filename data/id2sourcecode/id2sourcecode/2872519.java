    public ChannelManager getChannelManager() {
        if (logger.isDebugEnabled()) {
            logger.debug("getChannelManager() - start");
        }
        if (channelManager == null) {
            channelManager = new ChannelManager(this);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getChannelManager() - end");
        }
        return channelManager;
    }
