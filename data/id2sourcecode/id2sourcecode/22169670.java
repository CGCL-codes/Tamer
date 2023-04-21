    public SSOSession[] listUserSessions(String username) {
        try {
            if (logger.isTraceEnabled()) logger.trace("Listing SSO Sessions from MBean. User:" + username);
            IdPChannel channel = (IdPChannel) serviceProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            Collection<SSOSession> sessions = mgr.getUserSessions(username);
            return sessions.toArray(new SSOSession[sessions.size()]);
        } catch (Exception e) {
            logger.error("Cannot find sessions: " + e.getMessage(), e);
        }
        return null;
    }
