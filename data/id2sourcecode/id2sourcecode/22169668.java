    public TabularData listUserSessionsAsTable(String username) {
        try {
            if (logger.isTraceEnabled()) logger.trace("Listing SSO Sessions from MBean. User:" + username);
            IdPChannel channel = (IdPChannel) serviceProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            Collection<SSOSession> sessions = mgr.getUserSessions(username);
            List<JmxSSOSession> jmxSessions = new ArrayList<JmxSSOSession>(sessions.size());
            for (SSOSession session : sessions) {
                jmxSessions.add(new JmxSSOSession(session));
            }
            TabularData table = JmxSSOSession.tableFrom(jmxSessions);
            return table;
        } catch (Exception e) {
            logger.error("Cannot find sessions: " + e.getMessage(), e);
        }
        return null;
    }
