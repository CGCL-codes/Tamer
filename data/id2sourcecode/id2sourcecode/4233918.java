    public long getTotalDestroyedSessions() {
        try {
            SPChannel channel = (SPChannel) identityProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            return mgr.getStatsDestroyedSessions();
        } catch (Exception e) {
            logger.error("Cannot find SSO destroyed count");
            return -1;
        }
    }
