    protected void triggerIdPInitiatedSLO(IdPSecurityContext secCtx) throws SSOException, IdentityMediationException {
        if (logger.isTraceEnabled()) logger.trace("Triggering IDP Initiated SLO from MBean for Security Context " + secCtx);
        EndpointDescriptor ed = resolveIdpInitiatedSloEndpoint(identityProvider);
        if (logger.isDebugEnabled()) logger.debug("Using IDP Initiated SLO endpoint " + ed);
        IDPInitiatedLogoutRequestType sloRequest = new IDPInitiatedLogoutRequestType();
        sloRequest.setID(uuidGenerator.generateId());
        sloRequest.setSsoSessionId(secCtx.getSessionIndex());
        if (logger.isTraceEnabled()) logger.trace("Sending SLO Request " + sloRequest.getID() + " to IDP " + identityProvider.getName() + " using endpoint " + ed.getLocation());
        SSOResponseType sloResponse = (SSOResponseType) identityProvider.getChannel().getIdentityMediator().sendMessage(sloRequest, ed, identityProvider.getChannel());
        if (logger.isTraceEnabled()) logger.trace("Recevied SLO Response " + sloResponse.getID() + " from IDP " + identityProvider.getName() + " using endpoint " + ed.getLocation());
    }
