    public long getSessionsCount() {
        try {
            SPChannel channel = (SPChannel) serviceProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            return mgr.getStatsCurrentSessions();
        } catch (Exception e) {
            logger.error("Cannot find SSO Sessions count");
            return -1;
        }
    }
