    public TabularData listSessionsAsTable() {
        try {
            if (logger.isTraceEnabled()) logger.trace("Listing all SSO Sessions from MBean");
            SPChannel channel = (SPChannel) identityProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            Collection<SSOSession> sessions = mgr.getSessions();
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
