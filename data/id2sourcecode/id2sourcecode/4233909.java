    public boolean invalidateUserSessions(String username) {
        boolean invalidated = true;
        try {
            if (logger.isTraceEnabled()) logger.trace("Invalidating SSO Sessions from MBean for user:" + username);
            SPChannel channel = (SPChannel) identityProvider.getChannel();
            SSOSessionManager mgr = channel.getSessionManager();
            Collection<SSOSession> sessions = mgr.getUserSessions(username);
            for (SSOSession session : sessions) {
                try {
                    String sessionId = session.getId();
                    if (logger.isTraceEnabled()) logger.trace("Invalidating SSO Session from MBean. Session ID:" + sessionId);
                    ProviderStateContext ctx = new ProviderStateContext(identityProvider, applicationContext.getClassLoader());
                    LocalState state = ctx.retrieve(IdentityProviderConstants.SEC_CTX_SSOSESSION_KEY, sessionId);
                    IdPSecurityContext secCtx = (IdPSecurityContext) state.getValue(identityProvider.getName().toUpperCase() + "_SECURITY_CTX");
                    if (secCtx == null) {
                        if (logger.isDebugEnabled()) logger.debug("IdP Security Context not found for SSO Session ID: " + sessionId);
                        continue;
                    }
                    triggerIdPInitiatedSLO(secCtx);
                    if (logger.isDebugEnabled()) logger.debug("SSO Session invalidated from MBean: " + sessionId);
                } catch (Exception e) {
                    logger.error("Cannot invalidate SSO Session from MBean: " + e.getMessage(), e);
                    invalidated = false;
                }
            }
        } catch (Exception e) {
            logger.error("Cannot invalidate SSO Sessions from MBean for username: " + username + ".  " + e.getMessage(), e);
            invalidated = false;
        }
        return invalidated;
    }
