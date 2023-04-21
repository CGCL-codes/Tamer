    protected SPSecurityContext updateSPSecurityContext(SPSecurityContext secCtx, CamelMediationExchange exchange) throws SSOException, SSOSessionException {
        if (logger.isDebugEnabled()) logger.debug("Updating SP Security Context for " + secCtx.getSessionIndex());
        IdPChannel idPChannel = (IdPChannel) getProvider().getChannel();
        SSOSessionManager ssoSessionManager = idPChannel.getSessionManager();
        ssoSessionManager.accessSession(secCtx.getSessionIndex());
        if (logger.isDebugEnabled()) logger.debug("Updated SP security context " + secCtx);
        return secCtx;
    }
