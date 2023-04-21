    public long getMaxSessionsCount() {
        try {
            SPChannel channel = (SPChannel) serviceProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            return mgr.getStatsMaxSessions();
        } catch (Exception e) {
            logger.error("Cannot find SSO max sessions count");
            return -1;
        }
    }
