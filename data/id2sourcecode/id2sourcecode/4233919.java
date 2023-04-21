    public long getMaxSessionsCount() {
        try {
            SPChannel channel = (SPChannel) identityProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            return mgr.getStatsMaxSessions();
        } catch (Exception e) {
            logger.error("Cannot find SSO max sessions count");
            return -1;
        }
    }
