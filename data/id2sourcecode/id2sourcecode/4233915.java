    public long getMaxInactiveInterval() {
        try {
            if (logger.isTraceEnabled()) logger.trace("Listing all SSO Sessions from MBean");
            SPChannel channel = (SPChannel) identityProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            return mgr.getMaxInactiveInterval();
        } catch (Exception e) {
            logger.error("Cannot find SSO Sessions max inactive interval");
            return -1;
        }
    }
