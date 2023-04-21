    public MediationMessage createMessage(CamelMediationMessage message) {
        CamelMediationExchange samlR2exchange = message.getExchange();
        Exchange exchange = samlR2exchange.getExchange();
        logger.debug("Create Message Body from exchange " + exchange.getClass().getName());
        Message in = exchange.getIn();
        if (in.getBody() instanceof MessageContentsList) {
            MessageContentsList mclIn = (MessageContentsList) in.getBody();
            logger.debug("Using CXF Message Content : " + mclIn.get(0));
            FederatedLocalProvider p = null;
            if (getChannel() instanceof FederationChannel) {
                p = ((FederationChannel) getChannel()).getProvider();
            } else if (getChannel() instanceof BindingChannel) {
                p = ((BindingChannel) getChannel()).getProvider();
            } else if (getChannel() instanceof ClaimChannel) {
                p = ((ClaimChannel) getChannel()).getProvider();
            }
            MediationState state = null;
            LocalState lState = null;
            if (p != null) {
                if (logger.isDebugEnabled()) logger.debug("Attempting to retrieve provider state using JOSSO Backchannel messasge information");
                Object content = mclIn.get(0);
                if (logger.isTraceEnabled()) logger.trace("JOSSO Backchannel message " + content);
                if (content != null) {
                    String ssoSessionId = null;
                    String assertionId = null;
                    try {
                        Method getSsoSessionId = content.getClass().getMethod("getSsoSessionId");
                        ssoSessionId = (String) getSsoSessionId.invoke(content);
                    } catch (NoSuchMethodException e) {
                        if (logger.isTraceEnabled()) logger.trace(e.getMessage());
                    } catch (Exception e) {
                        logger.error("Cannot get SSO Session ID from JOSSO backchannel message: " + e.getMessage(), e);
                    }
                    try {
                        Method getSessionId = content.getClass().getMethod("getSessionId");
                        ssoSessionId = (String) getSessionId.invoke(content);
                    } catch (NoSuchMethodException e) {
                        if (logger.isTraceEnabled()) logger.trace(e.getMessage());
                    } catch (Exception e) {
                        logger.error("Cannot get SSO Session ID from JOSSO backchannel message: " + e.getMessage(), e);
                    }
                    try {
                        Method getAssertionId = content.getClass().getMethod("getAssertionId");
                        assertionId = (String) getAssertionId.invoke(content);
                    } catch (NoSuchMethodException e) {
                        if (logger.isTraceEnabled()) logger.trace(e.getMessage());
                    } catch (Exception e) {
                        logger.error("Cannot get SSO Assertion ID from JOSSO backchannel message: " + e.getMessage(), e);
                    }
                    ProviderStateContext ctx = createProviderStateContext();
                    if (lState == null && ssoSessionId != null) {
                        if (logger.isDebugEnabled()) logger.debug("Attempting to restore provider state based on SSO Session ID " + ssoSessionId);
                        int retryCount = getRetryCount();
                        if (retryCount > 0) {
                            lState = ctx.retrieve("ssoSessionId", ssoSessionId, retryCount, getRetryDelay());
                        } else {
                            lState = ctx.retrieve("ssoSessionId", ssoSessionId);
                        }
                    }
                    if (lState == null && assertionId != null) {
                        if (logger.isDebugEnabled()) logger.debug("Attempting to restore provider state based on Assertion ID " + assertionId);
                        int retryCount = getRetryCount();
                        if (retryCount > 0) {
                            lState = ctx.retrieve("assertionId", assertionId, retryCount, getRetryDelay());
                        } else {
                            lState = ctx.retrieve("assertionId", assertionId);
                        }
                    }
                }
            } else {
                logger.warn("No provider found for channel " + channel.getName());
            }
            if (lState == null) {
                state = createMediationState(exchange);
                lState = state.getLocalState();
                if (logger.isDebugEnabled()) logger.debug("Creating new Local State instance " + lState.getId() + " for " + channel.getName());
            } else {
                if (logger.isDebugEnabled()) logger.debug("Using Local State instance " + lState.getId() + " for " + channel.getName());
                state = new MediationStateImpl(lState);
            }
            return new MediationMessageImpl(in.getMessageId(), mclIn.get(0), null, null, null, state);
        } else {
            throw new IllegalArgumentException("Unknown message type " + in.getBody());
        }
    }
