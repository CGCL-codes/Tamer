    public long getTotalDestroyedSessions() {
        try {
            SPChannel channel = (SPChannel) serviceProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            return mgr.getStatsCreatedSessions();
        } catch (Exception e) {
            logger.error("Cannot find SSO destroyed count");
            return -1;
        }
    }
