    public long getTotalCreatedSessions() {
        try {
            SPChannel channel = (SPChannel) identityProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            return mgr.getStatsCreatedSessions();
        } catch (Exception e) {
            logger.error("Cannot find SSO created sessions count");
            return -1;
        }
    }
