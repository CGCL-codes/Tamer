    public SSOSession[] listSessions() {
        try {
            if (logger.isTraceEnabled()) logger.trace("Listing all SSO Sessions from MBean");
            IdPChannel channel = (IdPChannel) serviceProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            Collection<SSOSession> sessions = mgr.getSessions();
            return sessions.toArray(new SSOSession[sessions.size()]);
        } catch (Exception e) {
            logger.error("Cannot find sessions: " + e.getMessage(), e);
        }
        return null;
    }
