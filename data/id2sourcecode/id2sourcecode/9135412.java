    protected CircleOfTrustMemberDescriptor resolveSpAlias(SPChannel spChannel, IDPInitiatedAuthnRequestType ssoAuthnReq) throws SSOException {
        CircleOfTrustMemberDescriptor spDescr = null;
        SSOIDPMediator mediator = (SSOIDPMediator) spChannel.getIdentityMediator();
        if (spChannel.getTargetProvider() != null) {
            Provider sp = spChannel.getTargetProvider();
            if (sp instanceof FederatedRemoteProvider) {
                FederatedRemoteProvider spr = (FederatedRemoteProvider) sp;
                if (spr.getMembers().size() > 0) {
                    if (logger.isTraceEnabled()) logger.trace("Using first member descriptor for remote SP provider " + sp.getName());
                    spDescr = spr.getMembers().get(0);
                } else {
                    logger.error("No Circle of Trust Member descriptor found for remote SP Definition " + spr.getName());
                }
            } else {
                FederatedLocalProvider spl = (FederatedLocalProvider) sp;
                if (spl.getChannels() != null) {
                    for (Channel c : spl.getChannels()) {
                        if (c instanceof FederationChannel) {
                            FederationChannel fc = (FederationChannel) c;
                            if (fc.getTargetProvider() != null && fc.getTargetProvider().getName().equals(spChannel.getProvider().getName())) {
                                if (logger.isTraceEnabled()) logger.trace("Using SP Alias " + fc.getMember().getAlias() + " from channel " + fc.getName());
                                spDescr = fc.getMember();
                            }
                        }
                    }
                }
                if (spDescr == null) {
                    if (logger.isTraceEnabled()) logger.trace("Using SP Alias " + spl.getChannel().getMember().getAlias() + " from default channel " + spl.getChannel().getName());
                    spDescr = spl.getChannel().getMember();
                }
            }
        } else {
            String spAlias = mediator.getPreferredSpAlias();
            CircleOfTrustManager cotManager = spChannel.getProvider().getCotManager();
            if (ssoAuthnReq != null) {
                for (RequestAttributeType a : ssoAuthnReq.getRequestAttribute()) {
                    if (a.getName().equals("atricore_sp_id")) {
                        spDescr = cotManager.loolkupMemberById(a.getValue());
                        break;
                    }
                    if (a.getName().equals("atricore_sp_alias")) {
                        spDescr = cotManager.lookupMemberByAlias(a.getValue());
                        break;
                    }
                }
            }
            if (spDescr == null) spDescr = cotManager.lookupMemberByAlias(spAlias);
            if (logger.isTraceEnabled()) logger.trace("Using Preferred SP Alias " + spAlias);
            if (spDescr == null) {
                throw new SSOException("Cannot find SP for AuthnRequest ");
            }
        }
        if (logger.isDebugEnabled()) logger.debug("Resolved SP " + (spDescr != null ? spDescr.getAlias() : "NULL"));
        return spDescr;
    }
